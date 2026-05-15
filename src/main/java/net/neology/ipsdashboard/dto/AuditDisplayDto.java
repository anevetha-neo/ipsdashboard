package net.neology.ipsdashboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.neology.ipsdashboard.entity.AuditSummary;
import net.neology.ipsdashboard.entity.BaseEntity;
import net.neology.ipsdashboard.entity.TruthRun;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuditDisplayDto extends BaseEntity {
    
    private List<AuditSummary> auditSummaryCol;
    
    private List<TruthRun>  truthRunCol;
}