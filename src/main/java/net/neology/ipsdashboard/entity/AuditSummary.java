package net.neology.ipsdashboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "AUDIT_REPORT", schema = "IPS")
@Getter
@Setter
@NoArgsConstructor
public class AuditSummary {

    @Id
    @Column(name = "AUDIT_REPORT_ORD")
    private Long auditReportOrd;

    @Column(name = "AUDITOR")
    private Long auditor;

    @Column(name = "REVIEWER")
    private Long reviewer;

    @Column(name = "CREATED_DATE")
    private LocalDate auditDate;

    @Column(name = "START_DATE")
    private LocalDate reviewStartDate;

    @Column(name = "END_DATE")
    private LocalDate reviewEndDate;

    @Column(name = "AUDIT_ITEM_COUNT")
    private int auditItemCount;

    @Column(name = "PASSED_COUNT")
    private int passedCount;

    @Column(name = "FAILED_COUNT")
    private int failedCount;

    @Column(name = "SKIPPED_COUNT")
    private int skippedCount;

    @Column(name = "PASSED_PERCENTAGE")
    private float passPercent;

    @Column(name = "FAILED_PERCENTAGE")
    private float failPercent;

    @Transient private String startDate;
    @Transient private String endDate;
    @Transient private String auditorName;
    @Transient private String reviewerName;
}