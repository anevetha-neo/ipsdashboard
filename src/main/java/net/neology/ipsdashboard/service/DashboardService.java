package net.neology.ipsdashboard.service;

import jakarta.annotation.PostConstruct;
import net.neology.ipsdashboard.dto.*;
import net.neology.ipsdashboard.entity.AuditSummary;
import net.neology.ipsdashboard.entity.DailyReviewStats;
import net.neology.ipsdashboard.entity.TruthRun;
import net.neology.ipsdashboard.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);

    private final ActiveTxnLogRepository activeTxnLogRepository;
    private final DailyReviewStatsRepository dailyReviewStatsRepository;
    private final RequestRepository requestRepository;
    private final AuditSummaryRepository auditSummaryRepository;
    private final TruthRunRepository truthRunRepository;
    private final SecUserRepository secUserRepository;

    private volatile DashboardStatsDto cachedStats;

    public DashboardService(
            ActiveTxnLogRepository activeTxnLogRepository,
            DailyReviewStatsRepository dailyReviewStatsRepository,
            RequestRepository requestRepository,
            AuditSummaryRepository auditSummaryRepository,
            TruthRunRepository truthRunRepository,
            SecUserRepository secUserRepository) {
        this.activeTxnLogRepository = activeTxnLogRepository;
        this.dailyReviewStatsRepository = dailyReviewStatsRepository;
        this.requestRepository = requestRepository;
        this.auditSummaryRepository = auditSummaryRepository;
        this.truthRunRepository = truthRunRepository;
        this.secUserRepository = secUserRepository;
    }

    public DashboardStatsDto getDashboardStats() {
        return cachedStats;
    }

    public DashboardStatsDto refreshDashboardStats() {
        long start = System.currentTimeMillis();
        LOGGER.info("Refreshing full dashboard...");
        DashboardStatsDto dto = new DashboardStatsDto();
        Object oldest75A = activeTxnLogRepository.findOldestPendingForFacility("75A");
        Object oldest75B = activeTxnLogRepository.findOldestPendingForFacility("75B");
        dto.setIpsTimeHorizon75A(calculateTimeHorizon(oldest75A));
        dto.setIpsTimeHorizon75B(calculateTimeHorizon(oldest75B));
        LOGGER.info("Populated Time Horizon data.");
        List<Object[]> backlog = activeTxnLogRepository.countInProgressByFacility();
        int backlog75A = 0;
        int backlog75B = 0;
        for (Object[] o : backlog) {
            int count = ((Number) o[0]).intValue();
            String facility = String.valueOf(o[1]);
            if ("75A".equals(facility)) {
                backlog75A = count;
            } else {
                backlog75B = count;
            }
        }
        dto.setBacklog75A(formatNumber(backlog75A));
        dto.setBacklog75B(formatNumber(backlog75B));
        dto.setTotalBacklog(backlog75A + backlog75B);
        LOGGER.info("Populated Backlog data.");
        LocalDate today = LocalDate.now();
        List<DailyReviewStats> dailyList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            long perDayStart = System.currentTimeMillis();
            LocalDate day = today.minusDays(i);
            List<DailyReviewStats> perDay = dailyReviewStatsRepository.findByReviewDate(day);
            DailyReviewStats agg = new DailyReviewStats(day);
            for (DailyReviewStats d : perDay) {
                agg.setTotalCount(agg.getTotalCount() + d.getTotalCount());
                agg.setAutoComplete(agg.getAutoComplete() + d.getAutoComplete());
                agg.setTrComplete(agg.getTrComplete() + d.getTrComplete());
                agg.setCodeOff(agg.getCodeOff() + d.getCodeOff());
                agg.setSingleMir(agg.getSingleMir() + d.getSingleMir());
                agg.setDoubleMir(agg.getDoubleMir() + d.getDoubleMir());
            }
            agg.setCompleted(agg.getSingleMir() + agg.getDoubleMir());
            dailyList.add(0, agg);
            LOGGER.debug("dashboard daily aggregation for {} took {} ms",
                    day, System.currentTimeMillis() - perDayStart);
        }
        dto.setDailyReviewStats(dailyList);
        LOGGER.info("Populated Daily review stats data.");
        dto.setQueueStatsList(buildQueueStats());
        LOGGER.info("Populated Queue Statistics data.");
        Collection<UserStatsDto> users = new ArrayList<>();

        LocalDateTime startTs = today.atStartOfDay();
        LocalDateTime endTs = today.plusDays(1).atStartOfDay();

        List<Object[]> reviewerRows = secUserRepository.reviewerStats(startTs, endTs);
        for (Object[] row : reviewerRows) {
            String userId = String.valueOf(row[0]);
            int totalCount = ((Number) row[1]).intValue();
            int codeOffCount = row[2] == null ? 0 : ((Number) row[2]).intValue();
            UserStatsDto dtoUser = new UserStatsDto(userId, totalCount);
            dtoUser.setCodeOffCount(codeOffCount);
            users.add(dtoUser);
        }
        dto.setUserStats(users);
        LOGGER.info("Populated user stats data.");
        dto.setBackLogDays(buildBacklogDays(today));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
        dto.setRefreshTime((LocalDateTime.now()).format(dtf));
        dto.setProcessingTime(System.currentTimeMillis() - start);
        cachedStats = dto;
        LOGGER.info("Full dashboard refreshed in {} ms", System.currentTimeMillis() - start);
        return dto;
    }

    private List<QueueStatsDto> buildQueueStats() {
        long start = System.currentTimeMillis();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
        NumberFormat nf = NumberFormat.getNumberInstance();
        List<Object[]> statusNameRows = requestRepository.findQueueStatusNames();
        Map<Integer, QueueStatsDto> byStatusOrd = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        for (Object[] row : statusNameRows) {
            int ord = ((Number) row[0]).intValue();
            String name = String.valueOf(row[1]);
            QueueStatsDto dto = new QueueStatsDto();
            dto.setQueueName(name);
            dto.setMinDate(now.format(dtf));
            dto.setQueueLength("0");
            byStatusOrd.put(ord, dto);
        }
        List<Object[]> statsRows = requestRepository.findQueueStats();
        for (Object[] row : statsRows) {
            int statusOrd = ((Number) row[0]).intValue();
            QueueStatsDto dto = byStatusOrd.get(statusOrd);
            if (dto == null) {
                continue;
            }
            LocalDateTime oldest;
            Object minDateObj = row[1];
            if (minDateObj instanceof java.sql.Timestamp ts) {
                oldest = ts.toLocalDateTime();
            } else if (minDateObj instanceof java.sql.Date d) {
                oldest = d.toLocalDate().atStartOfDay();
            } else {
                oldest = now;
            }
            int count = ((Number) row[2]).intValue();
            dto.setMinDate(oldest.format(dtf));
            dto.setQueueLength(formatNumber(count));
        }
        List<QueueStatsDto> result = new ArrayList<>(byStatusOrd.values());
        LOGGER.info("Time elapsed for getQueueStats (legacy-equivalent): {} ms", System.currentTimeMillis() - start);
        return result;
    }

    private String formatNumber(int value) {
        return java.text.NumberFormat.getNumberInstance().format(value);
    }

    private String calculateTimeHorizon(Object dbValue) {
        if (dbValue == null) {
            return "0";
        }
        LocalDateTime oldest;
        if (dbValue instanceof java.sql.Timestamp ts) {
            oldest = ts.toLocalDateTime();
        } else {
            return "0";
        }
        return oldest.format(
                java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")
        );
    }

    public AuditDisplayDto getAuditDisplay(String dateStr) {
        long start = System.currentTimeMillis();
        LocalDate date = LocalDate.parse(dateStr);
        LocalDate nextDay = date.plusDays(1);
        LOGGER.info("Fetching audit and truthRun for {}", date);
        List<AuditSummary> auditList = auditSummaryRepository.findByAuditDateBetween(date, nextDay);
        List<TruthRun> truthRunList = truthRunRepository.findByCreationDateBetween(date.atStartOfDay(), nextDay.atStartOfDay());
        AuditDisplayDto dto = new AuditDisplayDto();
        dto.setAuditSummaryCol(auditList);
        dto.setTruthRunCol(truthRunList);

        LOGGER.info("Audit API completed in {} ms", System.currentTimeMillis() - start);
        return dto;
    }

    private List<BacklogDaysDto> buildBacklogDays(LocalDate today) {
        long start = System.currentTimeMillis();
        LocalDate startDay = today.minusDays(7);
        LocalDate endDayExclusive = today.plusDays(1);
        LocalDateTime startTs = startDay.atStartOfDay();
        LocalDateTime endTs = endDayExclusive.atStartOfDay();
        List<Object[]> pendingRows = activeTxnLogRepository.pendingBacklogByDay(startTs, endTs);
        List<Object[]> receivedRows = dailyReviewStatsRepository.totalReceivedByDay(startDay, endDayExclusive);
        Map<LocalDate, Integer> pendingMap = pendingRows.stream()
                .collect(Collectors.toMap(
                        row -> ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate(),
                        row -> ((Number) row[1]).intValue()
                ));
        Map<LocalDate, Integer> receivedMap = receivedRows.stream()
                .collect(Collectors.toMap(
                        row -> ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate(),
                        row -> ((Number) row[1]).intValue()
                ));
        List<BacklogDaysDto> result = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            LocalDate day = startDay.plusDays(i);
            int pending = pendingMap.getOrDefault(day, 0);
            int totalReceived = receivedMap.getOrDefault(day, 0);
            int processed = Math.max(totalReceived - pending, 0);
            BacklogDaysDto dto = new BacklogDaysDto();
            dto.setBacklogDate(day.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
            dto.setPending(pending);
            dto.setProcessed(processed);
            dto.setTotalReceived(totalReceived);
            result.add(dto);
        }
        LOGGER.info("BacklogDays built in {} ms", System.currentTimeMillis() - start);
        return result;
    }

    private LocalDate mapToLocalDate(Object dbValue) {
        if (dbValue instanceof java.sql.Timestamp ts) {
            return ts.toLocalDateTime().toLocalDate();
        } else if (dbValue instanceof java.sql.Date d) {
            return d.toLocalDate();
        } else {
            throw new IllegalArgumentException("Unsupported date type: " + dbValue);
        }
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing dashboard cache on startup");
        try {
            refreshDashboardStats();
        } catch (Exception e) {
            LOGGER.warn("Initial dashboard load failed", e);
        }
    }
}