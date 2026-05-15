package net.neology.ipsdashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "TIME_HORIZON", schema = "IPS")
@Getter
@Setter
@NoArgsConstructor
public class TimeHorizon extends BaseEntity {

    @Id
    @Column(name = "TIME_HORIZON_ORD")
    private Long timeHorizonOrd;
    
    @Column(name = "FACILITY_ORD")
    private long facilityOrd;
    
    @Column(name = "TIME_HORIZON_DATE")
    private LocalDateTime timeHorizonDate;
}
