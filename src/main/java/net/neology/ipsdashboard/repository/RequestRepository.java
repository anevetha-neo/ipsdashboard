package net.neology.ipsdashboard.repository;

import net.neology.ipsdashboard.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, String> {

    @Query(value = "select count(*), r.facility_code, r.status from ips.request r " +
            "where r.transaction_date between ?1 and ?2 group by r.facility_code, r.status",
            nativeQuery = true)
    List<Object[]> statusCountByDate(LocalDate start, LocalDate end);

    @Query(value = "select count(*), r.status from ips.request r " +
            "where r.transaction_date between ?1 and ?2 and r.facility_code = ?3 group by r.status",
            nativeQuery = true)
    List<Object[]> statusCountByDateAndFacility(LocalDate start, LocalDate end, String facilityCode);
}