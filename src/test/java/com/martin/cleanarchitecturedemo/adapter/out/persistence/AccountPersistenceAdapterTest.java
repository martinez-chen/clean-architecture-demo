package com.martin.cleanarchitecturedemo.adapter.out.persistence;

import static com.martin.cleanarchitecturedemo.common.AccountTestData.defaultAccount;
import static com.martin.cleanarchitecturedemo.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

import com.martin.cleanarchitecturedemo.application.domain.model.Account;
import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import com.martin.cleanarchitecturedemo.application.domain.model.ActivityWindow;
import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

/**
 * AccountPersistenceAdapterTest 只加載與 JPA 相關的 Bean，如 Repository、Entity、Mapper 等。
 * <p>
 * 實際呼叫 Repository 進行數據庫操作，並驗證操作結果是否符合預期。
 */
@DataJpaTest
@Import({AccountPersistenceAdapter.class, AccountMapper.class})
class AccountPersistenceAdapterTest {

  /**
   * 使用 @Autowired 來注入 AccountPersistenceAdapter 實例。
   */
  @Autowired
  private AccountPersistenceAdapter adapterUnderTest;

  /**
   * 使用 @Autowired 來注入 ActivityRepository 實例。
   */
  @Autowired
  private ActivityRepository activityRepository;


  /**
   * 根據 AccountPersistenceAdapterTest.sql 初始化資料庫, 並驗證 loadAccount 方法是否正確載入資料
   * <p>
   * <ol>
   * <li> 載入帳戶
   * <li> 驗證帳戶活動數量
   * <li> 驗證帳戶餘額
   * </ol>
   */
  @Test
  @Sql("AccountPersistenceAdapterTest.sql")
  void loadAccount() {
    Account account = adapterUnderTest.loadAccount(new AccountId(1L),
        LocalDateTime.of(2018, 8, 10, 0, 0));

    assertThat(account.getActivityWindow().getActivities()).hasSize(2);
    assertThat(account.calculateBalance()).isEqualTo(Money.of(500));
  }

  /**
   * 驗證 updateActivities 方法是否正確更新資料
   * <p>
   */
  @Test
  void updateActivities() {
    Account account = defaultAccount()
        .withBaselineBalance(Money.of(555L))
        .withActivityWindow(
            new ActivityWindow(defaultActivity().withId(null).withMoney(Money.of(1L)).build()))
        .build();

    adapterUnderTest.updateActivities(account);

    assertThat(activityRepository.count()).isEqualTo(1);
    ActivityJpaEntity savedActivity = activityRepository.findAll().get(0);
    assertThat(savedActivity.getAmount()).isEqualTo(1L);
  }
}