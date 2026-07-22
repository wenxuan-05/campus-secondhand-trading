package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.ReportDTO;
import com.campus.secondhand.dto.ReportVO;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public Result<Void> createReport(@Valid @RequestBody ReportDTO dto) {
        reportService.createReport(dto, UserContext.getUserId());
        return Result.ok();
    }

    @GetMapping
    public Result<Page<ReportVO>> getReports(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status) {
        return Result.ok(reportService.getReports(page, pageSize, status));
    }

    @PutMapping("/{id}/handle")
    public Result<Void> handleReport(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Integer status = (Integer) body.get("status");
        String note = (String) body.getOrDefault("handlerNote", "");
        reportService.handleReport(id, status, note, UserContext.getUserId());
        return Result.ok();
    }
}
