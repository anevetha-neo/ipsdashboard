package net.neology.ipsdashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SEC_USER", schema = "IPS")
@Getter
@Setter
@NoArgsConstructor
public class SecUser extends BaseEntity {
    
    @Id
    @Column(name = "SEC_USER_ORD")
    private Long secUserOrd;
    
    @Column(name = "SEC_USER_ID")
    private String secUserId;
}
