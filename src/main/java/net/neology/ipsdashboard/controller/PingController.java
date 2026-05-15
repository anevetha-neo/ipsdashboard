package net.neology.ipsdashboard.controller;

import net.neology.ipsdashboard.dto.DashboardStatsDto;
import net.neology.ipsdashboard.service.DashboardService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/resources/ping")
public class PingController {

    private final DashboardService dashboardService;

    public PingController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String ping() {
        return "pong. Date : " + LocalDateTime.now()
                + " , stats is "
                + dashboardService.getDashboardStats();
    }

    @GetMapping("/refresh")
    public DashboardStatsDto refresh() {
        return dashboardService.refreshDashboardStats();
    }
}
