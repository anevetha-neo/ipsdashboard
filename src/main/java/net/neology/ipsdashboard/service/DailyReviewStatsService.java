package net.neology.ipsdashboard.service;

import net.neology.ipsdashboard.entity.DailyReviewStats;

import net.neology.ipsdashboard.repository.DailyReviewStatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyReviewStatsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReviewStatsService.class);

    private final DailyReviewStatsRepository repository;

    public DailyReviewStatsService(DailyReviewStatsRepository repository) {
        this.repository = repository;
    }

    public List<DailyReviewStats> getDailyReviewStats(String facilityCode, String endDateStr) {
        long start = System.currentTimeMillis();
        LocalDate endDate = LocalDate.parse(endDateStr);
        List<DailyReviewStats> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            long perDayStart = System.currentTimeMillis();
            LocalDate day = endDate.minusDays(i);
            List<DailyReviewStats> dailyData = repository.findByReviewDate(day);
            DailyReviewStats aggregated = new DailyReviewStats(day);
            for (DailyReviewStats d : dailyData) {
                if ("ALL".equals(facilityCode) || facilityCode.equals(d.getFacilityCode())) {
                    aggregated.setTotalCount(aggregated.getTotalCount() + d.getTotalCount());
                    aggregated.setAutoComplete(aggregated.getAutoComplete() + d.getAutoComplete());
                    aggregated.setTrComplete(aggregated.getTrComplete() + d.getTrComplete());
                    aggregated.setCodeOff(aggregated.getCodeOff() + d.getCodeOff());
                    aggregated.setSingleMir(aggregated.getSingleMir() + d.getSingleMir());
                    aggregated.setDoubleMir(aggregated.getDoubleMir() + d.getDoubleMir());
                }
            }
            aggregated.setCompleted(aggregated.getSingleMir() + aggregated.getDoubleMir());
            result.add(0, aggregated);

            LOGGER.debug("getDailyReviewStats day={} took {} ms", day, System.currentTimeMillis() - perDayStart);
        }
        LOGGER.info("Transaction API completed in {} ms", System.currentTimeMillis() - start);
        return result;
    }
}