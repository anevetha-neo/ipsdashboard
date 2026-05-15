package net.neology.ipsdashboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.neology.ipsdashboard.entity.BaseEntity;

@NoArgsConstructor
@Getter
@Setter
public class UserStatsDto extends BaseEntity {
    
    private String userId;
    
    private int codeOffCount;
    
    private int reviewCount;
    
    private int totalCount;

    public UserStatsDto(String userId, int totalCount) {
        this.userId = userId;
        this.reviewCount = totalCount;
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "UserStats{" + "userId=" + userId + ", codeOffCount=" + codeOffCount + ", reviewCount=" + reviewCount + ", totalCount=" + totalCount + '}';
    }
}