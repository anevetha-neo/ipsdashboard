package net.neology.ipsdashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SYS_VALUE", schema = "IPS")
@Setter
@Getter
@NoArgsConstructor
public class SysValue extends BaseEntity {

    @Id
    @Column(name = "SYS_VALUE_ORD")
    private Long sysValueOrd;
    
    @Column(name = "SYS_VALUE_NAME")
    private String sysValueName;
    
    @Column(name = "VAL")
    private String val;
}
