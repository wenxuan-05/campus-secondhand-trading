package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.ReportDTO;
import com.campus.secondhand.dto.ReportVO;
import com.campus.secondhand.entity.ChatMessage;
import com.campus.secondhand.entity.Report;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.ChatMessageMapper;
import com.campus.secondhand.mapper.ReportMapper;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    private final ChatMessageMapper chatMessageMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void createReport(ReportDTO dto, Long reporterId) {
        String reportType = dto.getReportType() != null ? dto.getReportType() : "message";

        if ("user".equals(reportType) || (dto.getMessageId() == null && dto.getReportedUserId() != null)) {
            // ========== User-level report ==========
            Long reportedUserId = dto.getReportedUserId();
            if (reportedUserId == null) {
                throw new BusinessException("请指定举报对象");
            }
            // Validate target user exists
            User targetUser = userMapper.selectById(reportedUserId);
            if (targetUser == null) {
                throw new BusinessException("被举报用户不存在");
            }
            // Prevent self-report
            if (reportedUserId.equals(reporterId)) {
                throw new BusinessException("不能举报自己");
            }
            // Prevent duplicate user reports
            long count = count(new LambdaQueryWrapper<Report>()
                    .eq(Report::getReporterId, reporterId)
                    .eq(Report::getReportedUserId, reportedUserId)
                    .eq(Report::getReportType, "user"));
            if (count > 0) {
                throw new BusinessException("您已举报过该用户，请等待处理");
            }

            Report report = new Report();
            report.setReporterId(reporterId);
            report.setReportedUserId(reportedUserId);
            report.setReportType("user");
            report.setReason(dto.getReason());
            report.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
            report.setStatus(0);
            save(report);
        } else {
            // ========== Message-level report (existing logic) ==========
            if (dto.getMessageId() == null) {
                throw new BusinessException("消息ID不能为空");
            }
            ChatMessage message = chatMessageMapper.selectById(dto.getMessageId());
            if (message == null) {
                throw new BusinessException("消息不存在");
            }
            // Prevent self-report
            if (message.getSenderId().equals(reporterId)) {
                throw new BusinessException("不能举报自己的消息");
            }
            // Prevent duplicate reports on the same message by the same user
            long count = count(new LambdaQueryWrapper<Report>()
                    .eq(Report::getMessageId, dto.getMessageId())
                    .eq(Report::getReporterId, reporterId));
            if (count > 0) {
                throw new BusinessException("您已举报过该消息，请等待处理");
            }

            Report report = new Report();
            report.setReporterId(reporterId);
            report.setReportedUserId(message.getSenderId());
            report.setMessageId(dto.getMessageId());
            report.setSessionId(message.getSessionId());
            report.setReportType("message");
            report.setReason(dto.getReason());
            report.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
            report.setStatus(0);
            save(report);
        }
    }

    @Override
    public Page<ReportVO> getReports(int page, int pageSize, Integer status) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<Report>()
                .eq(status != null, Report::getStatus, status)
                .orderByDesc(Report::getCreatedAt);
        Page<Report> reportPage = page(new Page<>(page, pageSize), wrapper);

        Page<ReportVO> voPage = new Page<>(page, pageSize, reportPage.getTotal());
        voPage.setRecords(reportPage.getRecords().stream().map(r -> {
            ReportVO vo = new ReportVO();
            vo.setId(r.getId());
            vo.setReporterId(r.getReporterId());
            vo.setReportedUserId(r.getReportedUserId());
            vo.setMessageId(r.getMessageId());
            vo.setSessionId(r.getSessionId());
            vo.setReportType(r.getReportType() != null ? r.getReportType() : "message");
            vo.setReason(r.getReason());
            vo.setDescription(r.getDescription());
            vo.setStatus(r.getStatus());
            vo.setHandlerNote(r.getHandlerNote());
            vo.setHandledBy(r.getHandledBy());
            vo.setHandledAt(r.getHandledAt());
            vo.setCreatedAt(r.getCreatedAt());

            // Status label
            if (r.getStatus() == 0) vo.setStatusLabel("待处理");
            else if (r.getStatus() == 1) vo.setStatusLabel("已处理");
            else vo.setStatusLabel("已驳回");

            // Reporter name
            User reporter = userMapper.selectById(r.getReporterId());
            vo.setReporterName(reporter != null ? reporter.getNickname() : "未知用户");

            // Reported user name
            User reported = userMapper.selectById(r.getReportedUserId());
            vo.setReportedUserName(reported != null ? reported.getNickname() : "未知用户");

            // Message content preview (only for message-level reports)
            if (r.getMessageId() != null) {
                ChatMessage msg = chatMessageMapper.selectById(r.getMessageId());
                if (msg != null) {
                    String content = msg.getContent();
                    vo.setMessageContent(content != null && content.length() > 100
                            ? content.substring(0, 100) + "..." : content);
                }
            } else {
                vo.setMessageContent("(用户举报)");
            }

            return vo;
        }).toList());
        return voPage;
    }

    @Override
    @Transactional
    public void handleReport(Long id, Integer status, String note, Long adminId) {
        Report report = getById(id);
        if (report == null) {
            throw new BusinessException("举报记录不存在");
        }
        if (report.getStatus() != 0) {
            throw new BusinessException("该举报已处理过");
        }
        report.setStatus(status);
        report.setHandlerNote(note != null ? note : "");
        report.setHandledBy(adminId);
        report.setHandledAt(LocalDateTime.now());
        updateById(report);
    }
}
