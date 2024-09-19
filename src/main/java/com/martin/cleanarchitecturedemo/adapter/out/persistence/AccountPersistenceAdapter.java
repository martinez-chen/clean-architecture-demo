package com.martin.cleanarchitecturedemo.adapter.out.persistence;

import com.martin.cleanarchitecturedemo.application.domain.model.Account;
import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import com.martin.cleanarchitecturedemo.application.port.out.LoadAccountPort;
import com.martin.cleanarchitecturedemo.application.port.out.UpdateAccountStatePort;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 操作帳戶持久化。
 * <p>
 * 實作載入帳戶和更新帳戶狀態的操作。
 */
@RequiredArgsConstructor
@Component
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

  private final SpringDataAccountRepository accountRepository;
  private final ActivityRepository activityRepository;
  private final AccountMapper accountMapper;


  /**
   * <ol>
   * <li>查找帳戶
   * <li>查找帳戶活動
   * <li>計算提款餘額
   * <li>計算存款餘額
   * <li>映射到領域實體
   * </ol>
   *
   * @param accountId    帳戶 ID
   * @param baselineDate 基準日期
   * @return 帳戶
   */
  @Override
  public Account loadAccount(AccountId accountId, LocalDateTime baselineDate) {
    AccountJpaEntity account =
        accountRepository.findById(accountId.getValue())
            .orElseThrow(EntityNotFoundException::new);

    List<ActivityJpaEntity> activities =
        activityRepository.findByOwnerSince(accountId.getValue(), baselineDate);

    Long withdrawalBalance = activityRepository
        .getWithdrawalBalanceUntil(
            accountId.getValue(),
            baselineDate)
        .orElse(0L);

    Long depositBalance = activityRepository
        .getDepositBalanceUntil(
            accountId.getValue(),
            baselineDate)
        .orElse(0L);
    return accountMapper.mapToDomainEntity(
        account,
        activities,
        withdrawalBalance,
        depositBalance);
  }


  @Override
  public void updateActivities(Account account) {
    account.getActivityWindow().getActivities().forEach(activity -> {
      if (activity.getId() == null) {
        activityRepository.save(accountMapper.mapToJpaEntity(activity));
      }
    });
  }
}
