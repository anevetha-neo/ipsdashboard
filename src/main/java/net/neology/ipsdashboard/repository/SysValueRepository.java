package net.neology.ipsdashboard.repository;

import net.neology.ipsdashboard.entity.SysValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysValueRepository extends JpaRepository<SysValue, Long> {

    Optional<SysValue> findBySysValueName(String sysValueName);
}