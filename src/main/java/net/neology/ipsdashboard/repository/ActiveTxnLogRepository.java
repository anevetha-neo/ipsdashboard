package net.neology.ipsdashboard.repository;

import net.neology.ipsdashboard.entity.ActiveTxnLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ActiveTxnLogRepository extends JpaRepository<ActiveTxnLog, String> {

    @Query("select count(a) from ActiveTxnLog a where a.status = 'IN_PROGRESS'")
    long countInProgress();

    @Query("select count(a), a.facilityCode from ActiveTxnLog a where a.status = 'IN_PROGRESS' group by a.facilityCode")
    List<Object[]> countInProgressByFacility();

    @Query(value = "select count(*) from ips.active_txn_log where status = 'IN_PROGRESS' and trunc(transaction_date) = ?1", nativeQuery = true)
    long dailyBacklog(LocalDate day);

    @Query(value = """
    select trunc(atl.transaction_date) as day,
           count(*) as pending
    from ips.active_txn_log atl
    where atl.status = 'IN_PROGRESS'
      and atl.transaction_date >= :startTs
      and atl.transaction_date < :endTs
    group by trunc(atl.transaction_date)
    """, nativeQuery = true)
    List<Object[]> pendingBacklogByDay(
            @Param("startTs") LocalDateTime startTs,
            @Param("endTs") LocalDateTime endTs
    );

    @Query(value = """
    select min(atl.transaction_date) 
    from ips.active_txn_log atl
    where atl.status = 'IN_PROGRESS'
      and atl.facility_code = :facility
""", nativeQuery = true)
    Object findOldestPendingForFacility(String facility);

    @Query(value = """
    select atl.status,
           min(atl.transaction_date),
           count(*)
    from ips.active_txn_log atl
    where atl.status = 'IN_PROGRESS'
    group by atl.status
""", nativeQuery = true)
    List<Object[]> queueStats();
}