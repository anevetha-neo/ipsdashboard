package net.neology.ipsdashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TRUTH_RUN", schema = "IPS")
public class TruthRun extends BaseEntity {
    
    @Id
    @Column(name = "TRUTH_RUN_ORD")
    private Long truthRunOrd;
    
    @Column(name = "TRUTH_SET_ORD")
    private Long truthSet;
    
    @Column(name = "INITIATED_BY_USER_ID")
    private Long initiatedUser;
    
    @Column(name = "USER_ID")
    private Long user;
    
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    
    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "SUMMARY_PERCENT_COMPLETE")
    private float completePercent;
    
    @Column(name = "SUMMARY_PERCENT_ACCURACY")
    private float accuracyPercent;
    
    @Transient
    private String runDate;
    
    @Transient
    private String userId;
    
    @Transient
    private String InitiatedUserId;
    
    @Transient
    private String createdDate;
    
    @Transient
    private String truthSetName;
}