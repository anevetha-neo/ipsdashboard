package net.neology.ipsdashboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ACTIVE_TXN_LOG", schema = "IPS")
@Getter
@Setter
@NoArgsConstructor
public class ActiveTxnLog {

    @Id
    @Column(name = "ACTIVE_TXN_LOG_ORD")
    private String activeTxnLogOrd;

    @Column(name = "TRANSACTION_DATE")
    private LocalDateTime transactionDate;

    @Column(name = "FACILITY_CODE")
    private String facilityCode;

    @Column(name = "STATUS")
    private String status;
}