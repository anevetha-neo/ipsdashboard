package net.neology.ipsdashboard.scheduler;

import net.neology.ipsdashboard.config.DashboardWebSocketHandler;
import net.neology.ipsdashboard.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class DashboardScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardScheduler.class);

    private final DashboardService dashboardService;
    private final DashboardWebSocketHandler wsHandler;

    public DashboardScheduler(
            DashboardService dashboardService,
            DashboardWebSocketHandler wsHandler) {

        this.dashboardService = dashboardService;
        this.wsHandler = wsHandler;
    }

    @Scheduled(fixedRate = 300000)
    public void refreshDashboard() {
        LOGGER.info("Scheduler triggered dashboard refresh");
        dashboardService.refreshDashboardStats();
        wsHandler.broadcast();
    }
}
