package net.neology.ipsdashboard.repository;

import net.neology.ipsdashboard.entity.SecUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SecUserRepository extends JpaRepository<SecUser, Long> {

    @Query(value = """
        select s.sec_user_id as userId,
               count(*) as totalCount,
               sum(case when p.code_off_code is not null then 1 else 0 end) as codeOffCount
        from ips.processor_decision p
        join ips.sec_user s on p.user_id = s.sec_user_ord
        where p.creation_date >= :startTs
          and p.creation_date <  :endTs
        group by s.sec_user_id
        """, nativeQuery = true)
    List<Object[]> reviewerStats(LocalDateTime startTs, LocalDateTime endTs);

    Optional<SecUser> findBySecUserId(String secUserId);
}