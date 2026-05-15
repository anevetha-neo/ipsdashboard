package net.neology.ipsdashboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.neology.ipsdashboard.entity.BaseEntity;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class BacklogDaysDto extends BaseEntity {
    
//    @JsonbDateFormat(value = "EEEE, MMMM dd, yyyy")
    private LocalDate backlogDate;
    
    private int totalReceived;
    
    private int pending;
    
    private int processed;

    public BacklogDaysDto(LocalDate backlogDate) {
        this.backlogDate = backlogDate;
    }
    
    public BacklogDaysDto(LocalDate backlogDate, int pending) {
        this.backlogDate = backlogDate;
        this.pending = pending;
        this.totalReceived = pending;
    }

    @Override
    public String toString() {
        return "BacklogDays{" + "backlogDate=" + backlogDate + ", totalReceived=" + totalReceived + ", pending=" + pending + ", processed=" + processed + '}';
    }
}