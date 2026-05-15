package net.neology.ipsdashboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "DAILY_REVIEW_STATS", schema = "IPS")
@Getter
@Setter
@NoArgsConstructor
public class DailyReviewStats extends BaseEntity {
    
    @Id
    @Column(name = "DAILY_REVIEW_STATS_ORD")
    @NonNull
    private Long dailyReviewStatsOrd;
    
//    @JsonbDateFormat(value = "EEEE, MMMM dd, yyyy")
    @Column(name = "REVIEW_DATE", updatable = false)
    private LocalDate reviewDate;
    
    @Column(name = "FACILITY_CODE", updatable = false)
    private String facilityCode;
    
    @Column(name = "TOTAL_COUNT", updatable = false)
    private int totalCount;
    
    @Column(name = "AUTO_COMPLETE", updatable = false)
    private int autoComplete;
    
    @Column(name = "TR_COMPLETE", updatable = false)
    private int trComplete;
    
    @Column(name = "CODE_OFF", updatable = false)
    private int codeOff;
    
    @Column(name = "SINGLE_MIR", updatable = false)
    private int singleMir;
    
    @Column(name = "DOUBLE_MIR", updatable = false)
    private int doubleMir;
    
    @Transient
    private int vsrCount;
    
    @Transient
    private int fReview;
    
    @Transient
    private int sReview;
    
    @Transient
    private int completed;
    
    @Transient
    private int codeOfRecommended;
    
    @Transient
    private int supervisorReview;

    public DailyReviewStats(Long dailyReviewStatsOrd, LocalDate reviewDate) {
        this.dailyReviewStatsOrd = dailyReviewStatsOrd;
        this.reviewDate = reviewDate;
    }

    public DailyReviewStats(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
}