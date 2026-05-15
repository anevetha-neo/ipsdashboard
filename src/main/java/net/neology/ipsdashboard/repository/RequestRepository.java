package net.neology.ipsdashboard.repository;

import net.neology.ipsdashboard.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, String> {

    @Query(value = """
        select rs.request_status_ord, rs.request_status
        from ips.request_status rs
        where rs.request_status_ord in (1,4,5,6,7,9,12)
        """, nativeQuery = true)
    List<Object[]> findQueueStatusNames();

    @Query(value = """
        select r.status, min(r.transaction_date), count(*)
        from ips.request r
        where r.status in (1,4,5,6,7,9,12)
        group by r.status
        """, nativeQuery = true)
    List<Object[]> findQueueStats();
}