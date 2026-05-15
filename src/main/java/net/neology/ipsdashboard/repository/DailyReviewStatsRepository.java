package net.neology.ipsdashboard.repository;

import net.neology.ipsdashboard.entity.DailyReviewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DailyReviewStatsRepository extends JpaRepository<DailyReviewStats, Long> {

    List<DailyReviewStats> findByReviewDate(LocalDate reviewDate);

    List<DailyReviewStats> findByReviewDateAndFacilityCode(LocalDate reviewDate, String facilityCode);

    @Query("select coalesce(sum(d.totalCount), 0) from DailyReviewStats d where d.reviewDate = :reviewDate")
    long sumTotalCountByReviewDate(LocalDate reviewDate);

    @Query(value = """
    select drs.review_date as day,
           nvl(sum(drs.total_count), 0) as totalReceived
    from ips.daily_review_stats drs
    where drs.review_date >= :startDate
      and drs.review_date < :endDate
    group by drs.review_date
    """, nativeQuery = true)
    List<Object[]> totalReceivedByDay(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}