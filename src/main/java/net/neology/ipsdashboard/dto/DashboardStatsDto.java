package net.neology.ipsdashboard.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.neology.ipsdashboard.entity.BaseEntity;
import net.neology.ipsdashboard.entity.DailyReviewStats;

@Getter
@Setter
@NoArgsConstructor
public class DashboardStatsDto extends BaseEntity {
    
    private String refreshTime = "";
    
    private String ipsTimeHorizon75A = "";
    
    private String ipsTimeHorizon75B = "";
    
    private int totalBacklog;
    
    private String backlog75A;
    
    private String backlog75B;
    
    private Collection<UserStatsDto> userStats = new ArrayList<>();
    
    private List<BacklogDaysDto> backLogDays = new ArrayList<>();
    
    private Collection<QueueStatsDto> queueStatsList = new ArrayList<>();
    
    private List<DailyReviewStats> dailyReviewStats = new ArrayList<>();
    
    private long processingTime;
}