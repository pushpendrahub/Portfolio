package com.portfolio.pushpendra.admin.controller;

import com.portfolio.pushpendra.admin.repository.VisitorLogRepository;
import com.portfolio.pushpendra.admin.service.VisitorLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class VisitorLogController {

    private final VisitorLogRepository visitorLogRepository;
    private final VisitorLogService visitorLogService;

    public VisitorLogController(VisitorLogRepository visitorLogRepository, VisitorLogService visitorLogService) {
        this.visitorLogRepository = visitorLogRepository;
        this.visitorLogService = visitorLogService;
    }

    @GetMapping("/visitors")
    @ResponseBody
    public List<Map<String, String>> getVisitorLogs() {
        return visitorLogService.getFormattedVisitorLogs(); // JSON response
    }

}
