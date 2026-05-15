package net.neology.ipsdashboard.repository;

import net.neology.ipsdashboard.entity.AuditSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AuditSummaryRepository extends JpaRepository<AuditSummary, Long> {

    List<AuditSummary> findByAuditDateBetween(LocalDate startDate, LocalDate endDate);
}
