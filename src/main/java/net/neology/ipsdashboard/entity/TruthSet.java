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
@Table(name = "TRUTH_SET", schema = "IPS")
@Getter
@Setter
@NoArgsConstructor
public class TruthSet extends BaseEntity {
    
    @Id
    @Column(name = "TRUTH_SET_ORD")
    private Long truthSetOrd;
    
    @Column(name = "TRUTH_SET_NAME")
    private String truthSetName;
    
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
}