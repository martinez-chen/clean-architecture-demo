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
 */
@DataJpaTest
@Import({AccountPersistenceAdapter.class,AccountMapper.class})
class AccountPersistenceAdapterTest {

  @Autowired
  private AccountPersistenceAdapter adapterUnderTest;

  @Autowired
  private ActivityRepository activityRepository;


  /**
   * 根據 AccountPersistenceAdapterTest.sql 初始化資料庫,
   * 並驗證 loadAccount 方法是否正確載入資料
   * <p>
   *   1. 載入帳戶
   *   2. 驗證帳戶活動數量
   *   3. 驗證帳戶餘額
   */
  @Test
  @Sql("AccountPersistenceAdapterTest.sql")
  void loadAccount(){
    Account account=adapterUnderTest.loadAccount(new AccountId(1L), LocalDateTime.of(2018, 8, 10, 0, 0));

    assertThat(account.getActivityWindow().getActivities()).hasSize(2);
    assertThat(account.calculateBalance()).isEqualTo(Money.of(500));
  }

  /**
   * 驗證 updateActivities 方法是否正確更新資料
   * <p>
   */
  @Test
  void updateActivities() {
    Account account= defaultAccount()
        .withBaselineBalance(Money.of(555L))
        .withActivityWindow(new ActivityWindow(defaultActivity().withId(null).withMoney(Money.of(1L)).build()))
        .build();

    adapterUnderTest.updateActivities(account);

    assertThat(activityRepository.count()).isEqualTo(1);
    ActivityJpaEntity savedActivity=activityRepository.findAll().get(0);
    assertThat(savedActivity.getAmount()).isEqualTo(1L);
  }
}