package com.martin.cleanarchitecturedemo.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityRepository extends JpaRepository<ActivityJpaEntity, Long> {

  /**
   * 查找帳戶異動紀錄。
   *
   * @param accountId 帳戶 ID
   * @param since     起始時間
   * @return 帳戶異動紀錄
   */
  @Query("SELECT a FROM ActivityJpaEntity a WHERE a.ownerAccountId = :accountId AND a.timestamp >= :since")
  List<ActivityJpaEntity> findByOwnerSince(@Param("accountId") long accountId,
      @Param("since") LocalDateTime since);

  /**
   * 取得截止時間前的存款總額。
   *
   * @param accountId 帳戶 ID
   * @param until     截止時間
   * @return 存款總額
   */
  @Query("select sum(a.amount) from ActivityJpaEntity a where a.targetAccountId = :accountId and a.ownerAccountId = :accountId and a.timestamp < :until")
  Optional<Long> getDepositBalanceUntil(@Param("accountId") long accountId,
      @Param("until") LocalDateTime until);


  /**
   * 取得截止時間前的提款總額。
   *
   * @param accountId 帳戶 ID
   * @param until     截止時間
   * @return 提款總額
   */
  @Query("select sum(a.amount) from ActivityJpaEntity a where a.sourceAccountId = :accountId and a.ownerAccountId = :accountId and a.timestamp < :until")
  Optional<Long> getWithdrawalBalanceUntil(@Param("accountId") long accountId,
      @Param("until") LocalDateTime until);
}
