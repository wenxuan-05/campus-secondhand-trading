package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.dto.ReportDTO;
import com.campus.secondhand.dto.ReportVO;
import com.campus.secondhand.entity.Report;

public interface ReportService extends IService<Report> {
    void createReport(ReportDTO dto, Long reporterId);
    Page<ReportVO> getReports(int page, int pageSize, Integer status);
    void handleReport(Long id, Integer status, String note, Long adminId);
}
