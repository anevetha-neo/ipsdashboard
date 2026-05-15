package net.neology.ipsdashboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.neology.ipsdashboard.entity.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
public class QueueStatsDto extends BaseEntity {
    
    private String queueName;
    
    private String minDate;
    
    private String queueLength;

    public QueueStatsDto(String queueName, String minDate, String queueLength) {
        this.queueName = queueName;
        this.minDate = minDate;
        this.queueLength = queueLength;
    }
}