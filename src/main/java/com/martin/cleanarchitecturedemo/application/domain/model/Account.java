package com.martin.cleanarchitecturedemo.application.domain.model;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

/**
 * 提供帳戶的當前概況。
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

  private final AccountId id;
  /**
   * 用來代表這段帳戶活動紀錄期間、第一項活動執行之前的帳戶基準餘額。 至於當前餘額則是基準餘額屬性，再加上所有活動所計算出的結果。
   */
  @Getter
  private final Money baselineBalance;
  /**
   * 只截取最近的交易紀錄。
   */
  @Getter
  private final ActivityWindow activityWindow;

  /**
   * Creates an {@link Account} entity without an ID. Use to create a new entity that is not yet
   * persisted.
   */
  public static Account withoutId(
      Money baselineBalance,
      ActivityWindow activityWindow) {
    return new Account(null, baselineBalance, activityWindow);
  }

  /**
   * Creates an {@link Account} entity with an ID. Use to reconstitute a persisted entity.
   */
  public static Account withId(
      AccountId accountId,
      Money baselineBalance,
      ActivityWindow activityWindow) {
    return new Account(accountId, baselineBalance, activityWindow);
  }

  public Optional<AccountId> getId() {
    return Optional.ofNullable(this.id);
  }

  /**
   * Calculates the total balance of the account by adding the activity values to the baseline
   * balance.
   * 翻譯：通過將活動值添加到基準餘額來計算帳戶的總餘額。
   */
  public Money calculateBalance() {
    return Money.add(
        this.baselineBalance,
        this.activityWindow.calculateBalance(this.id));
  }

  /**
   * 嘗試從此帳戶提取一定數量的金額。 如果成功，則創建一個具有負值的新活動。
   *
   * @return 如果提款成功則返回 true，否則返回 false。
   */
  public boolean withdraw(Money money, AccountId targetAccountId) {
    if (!mayWithdraw(money)) {
      return false;
    }
    Activity withdrawal = new Activity(
        this.id,
        this.id,
        targetAccountId,
        LocalDateTime.now(),
        money);
    this.activityWindow.addActivity(withdrawal);
    return true;
  }

  private boolean mayWithdraw(Money money) {
    return Money.add(
        this.calculateBalance(),
        money.negate()
    ).isPositiveOrZero();
  }

  /**
   * 嘗試向此帳戶存入一定數量的金額。 如果成功，則創建一個具有正值的新活動。
   *
   * @return 如果存款成功則返回 true，否則返回 false。
   */
  public boolean deposit(Money money, AccountId sourceAccountId) {
    Activity deposit = new Activity(
        this.id,
        sourceAccountId,
        this.id,
        LocalDateTime.now(),
        money);
    this.activityWindow.addActivity(deposit);
    return true;
  }

  @Value
  public static class AccountId {

    Long value;
  }
}
