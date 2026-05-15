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
@Table(name = "TXN_TRANSACTION", schema = "IPS")
@Getter
@Setter
@NoArgsConstructor
public class TxnTransaction extends BaseEntity {
    
    @Id
    @Column(name = "TXN_TRANSACTION_ID")
    private Long id;
    
    @Column(name = "TRANSACTION_DATE")
    private LocalDateTime transactionDate;
}