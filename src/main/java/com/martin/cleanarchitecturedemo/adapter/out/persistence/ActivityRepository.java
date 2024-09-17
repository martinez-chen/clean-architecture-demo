package com.martin.cleanarchitecturedemo.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityRepository extends JpaRepository<ActivityJpaEntity, Long> {

  @Query("SELECT a FROM ActivityJpaEntity a WHERE a.ownerAccountId = :accountId AND a.timestamp >= :since")
  List<ActivityJpaEntity> findByOwnerSince(@Param("accountId") long accountId,
     @Param("since") LocalDateTime since);

  @Query("select sum(a.amount) from ActivityJpaEntity a where a.targetAccountId = :accountId and a.ownerAccountId = :accountId and a.timestamp < :until")
  Optional<Long> getDepositBalanceUntil(@Param("accountId") long accountId,
      @Param("until") LocalDateTime until);


  @Query("select sum(a.amount) from ActivityJpaEntity a where a.sourceAccountId = :accountId and a.ownerAccountId = :accountId and a.timestamp < :until")
  Optional<Long> getWithdrawalBalanceUntil(@Param("accountId") long accountId,
      @Param("until") LocalDateTime until);
}
