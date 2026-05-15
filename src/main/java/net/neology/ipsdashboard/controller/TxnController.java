package net.neology.ipsdashboard.controller;

import net.neology.ipsdashboard.dto.DashboardStatsDto;
import net.neology.ipsdashboard.entity.DailyReviewStats;
import net.neology.ipsdashboard.service.DailyReviewStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resources/txn")
public class TxnController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TxnController.class);

    private final DailyReviewStatsService dailyReviewStatsService ;

    public TxnController(DailyReviewStatsService service) {
        this.dailyReviewStatsService  = service;
    }

    @GetMapping("/{facility}/{day}")
    public DashboardStatsDto search(@PathVariable String facility, @PathVariable String day) {
        long start = System.currentTimeMillis();
        LOGGER.info("Facility={}, date={}", facility, day);
        List<DailyReviewStats> stats = dailyReviewStatsService.getDailyReviewStats(facility, day);
        DashboardStatsDto response = new DashboardStatsDto();
        response.setDailyReviewStats(stats);
        response.setProcessingTime(System.currentTimeMillis() - start);
        return response;
    }
}
