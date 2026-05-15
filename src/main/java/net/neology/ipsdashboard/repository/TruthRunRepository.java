package net.neology.ipsdashboard.repository;

import net.neology.ipsdashboard.entity.TruthRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TruthRunRepository extends JpaRepository<TruthRun, Long> {

    List<TruthRun> findByCreationDateBetween(LocalDateTime start, LocalDateTime end);
}