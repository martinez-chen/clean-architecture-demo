package com.martin.cleanarchitecturedemo.application.domain;

import static com.martin.cleanarchitecturedemo.common.AccountTestData.defaultAccount;
import static com.martin.cleanarchitecturedemo.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

import com.martin.cleanarchitecturedemo.application.domain.model.Account;
import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import com.martin.cleanarchitecturedemo.application.domain.model.ActivityWindow;
import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import org.junit.jupiter.api.Test;

/**
 * 充血模型的 Account 類別包含了帳戶的基本資訊、活動窗口及相關的行為。
 * <p>
 * AccountTest 用來測試 Account 類別的行為。
 *
 */
class AccountTest {

  @Test
  void calculatesBalance() {
    AccountId accountId = new AccountId(1L);
    Account account = defaultAccount()
        .withAccountId(accountId)
        .withBaselineBalance(Money.of(555L))
        .withActivityWindow(new ActivityWindow(
            defaultActivity()
                .withTargetAccount(accountId)
                .withMoney(Money.of(999L)).build(),
            defaultActivity()
                .withTargetAccount(accountId)
                .withMoney(Money.of(1L)).build()))
        .build();

    Money balance = account.calculateBalance();

    assertThat(balance).isEqualTo(Money.of(1555L));
  }

  @Test
  void withdrawalSucceeds() {
    AccountId accountId = new AccountId(1L);
    Account account = defaultAccount()
        .withAccountId(accountId)
        .withBaselineBalance(Money.of(555L))
        .withActivityWindow(new ActivityWindow(
            defaultActivity()
                .withTargetAccount(accountId)
                .withMoney(Money.of(999L)).build(),
            defaultActivity()
                .withTargetAccount(accountId)
                .withMoney(Money.of(1L)).build()))
        .build();

    AccountId randomTargetAccount = new AccountId(99L);
    boolean success = account.withdraw(Money.of(555L), randomTargetAccount);

    assertThat(success).isTrue();
    assertThat(account.getActivityWindow().getActivities()).hasSize(3);
    assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L));
  }

  @Test
  void withdrawalFailure() {
    AccountId accountId = new AccountId(1L);
    Account account = defaultAccount()
        .withAccountId(accountId)
        .withBaselineBalance(Money.of(555L))
        .withActivityWindow(new ActivityWindow(
            defaultActivity()
                .withTargetAccount(accountId)
                .withMoney(Money.of(999L)).build(),
            defaultActivity()
                .withTargetAccount(accountId)
                .withMoney(Money.of(1L)).build()))
        .build();

    boolean success = account.withdraw(Money.of(1556L), new AccountId(99L));

    assertThat(success).isFalse();
    assertThat(account.getActivityWindow().getActivities()).hasSize(2);
    assertThat(account.calculateBalance()).isEqualTo(Money.of(1555L));
  }

  @Test
  void depositSuccess() {
    AccountId accountId = new AccountId(1L);
    Account account = defaultAccount()
        .withAccountId(accountId)
        .withBaselineBalance(Money.of(555L))
        .withActivityWindow(new ActivityWindow(
            defaultActivity()
                .withTargetAccount(accountId)
                .withMoney(Money.of(999L)).build(),
            defaultActivity()
                .withTargetAccount(accountId)
                .withMoney(Money.of(1L)).build()))
        .build();

    boolean success = account.deposit(Money.of(445L), new AccountId(99L));

    assertThat(success).isTrue();
    assertThat(account.getActivityWindow().getActivities()).hasSize(3);
    assertThat(account.calculateBalance()).isEqualTo(Money.of(2000L));
  }

}