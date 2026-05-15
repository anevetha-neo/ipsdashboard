package net.neology.ipsdashboard.repository;

import net.neology.ipsdashboard.entity.TxnTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface TxnTransactionRepository extends JpaRepository<TxnTransaction, Long> {

    long countByTransactionDateAfter(Date txnDate);

    @Query(value = "select count(*) from ips.txn_transaction where transaction_date > trunc(sysdate)", nativeQuery = true)
    long latestCount();
}