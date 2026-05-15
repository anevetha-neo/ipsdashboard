package net.neology.ipsdashboard.controller;

import net.neology.ipsdashboard.dto.AuditDisplayDto;
import net.neology.ipsdashboard.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources/audit")
public class AuditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditController.class);

    private final DashboardService dashboardService;

    public AuditController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{day}")
    public AuditDisplayDto search(@PathVariable String day) {
        LOGGER.info("Audit request for date {}", day);
        return dashboardService.getAuditDisplay(day);
    }
}