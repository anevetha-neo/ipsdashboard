package net.neology.ipsdashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "REQUEST", schema = "IPS")
@Getter
@Setter
public class Request  extends BaseEntity {

    @Id
    @Column(name = "REQUEST_ID")
    private String requestId;
    
    @Column(name = "STATUS")
    private int status;

    public Request() {
    }
}
