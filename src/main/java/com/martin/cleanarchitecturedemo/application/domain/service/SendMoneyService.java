package com.martin.cleanarchitecturedemo.application.domain.service;

import com.martin.cleanarchitecturedemo.application.port.in.SendMoneyCommand;
import com.martin.cleanarchitecturedemo.application.port.out.UpdateAccountStatePort;
import com.martin.cleanarchitecturedemo.application.domain.model.Account;
import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import com.martin.cleanarchitecturedemo.application.port.in.SendMoneyUseCase;
import com.martin.cleanarchitecturedemo.application.port.out.AccountLock;
import com.martin.cleanarchitecturedemo.application.port.out.LoadAccountPort;
import com.martin.cleanarchitecturedemo.common.UseCase;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

/**
 * 使用案例應該承擔的是業務規則（ business rules ）的驗證。
 *
 * <p>UpdateAccountStatePort 和 LoadAccountPort 是由儲存層轉接器實作的轉接埠介面（ port interfaces ）。
 * 如果經常一起使用，也可以組合成一個更廣泛的介面。甚至可以將該介面命名為 AccountRepository 。
 * <p>驗證業務規則（ business rule validation ）:使用案例應承擔的職責，業務規則在驗證時須根據「領域模型的當前狀態」而定
 * （例如，帳戶的餘額是否足夠）。語意驗證（ semantic validation ）。
 * <p>驗證輸入資料 （ input data validation ）: 輸入驗證不是使用案例應該承擔的職責，而是應用程式層的責任，
 * 但無法完全信任呼叫方代替使用案例執行必要的驗證，因此讓輸入模型（ input model ）自行驗證是一個好主意。語法驗證（ syntactic validation ）。
 */
@RequiredArgsConstructor
@UseCase
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

  private final LoadAccountPort loadAccountPort;
  private final AccountLock accountLock;
  private final UpdateAccountStatePort updateAccountStatePort;
  private final MoneyTransferProperties moneyTransferProperties;

  /**
   * <ol>
   * <li>驗證業務規則
   * <li>設定基準日期
   * <li>加載來源帳戶和目標帳戶
   * <li>獲取帳戶 ID
   * <li>鎖定來源帳戶並進行提款操作
   * <li>鎖定目標帳戶並進行存款操作
   * <li>更新帳戶狀態
   * <li>釋放帳戶鎖
   * <li>返回結果
   * </ol>
   *
   * @param command 用戶輸入的命令
   * @return 是否成功
   */
  @Override
  public boolean sendMoney(SendMoneyCommand command) {
    //  驗證業務規則
    checkThreshold(command);

    LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

    Account sourceAccount = loadAccountPort.loadAccount(
        command.sourceAccountId(),
        baselineDate);

    Account targetAccount = loadAccountPort.loadAccount(
        command.targetAccountId(),
        baselineDate);

    AccountId sourceAccountId = sourceAccount.getId()
        .orElseThrow(() -> new IllegalStateException("expected source account ID not to be empty"));
    AccountId targetAccountId = targetAccount.getId()
        .orElseThrow(() -> new IllegalStateException("expected target account ID not to be empty"));

    // 操作模型狀態
    accountLock.lockAccount(sourceAccountId);
    if (!sourceAccount.withdraw(command.money(), targetAccountId)) {
      accountLock.releaseAccount(sourceAccountId);
      return false;
    }

    accountLock.lockAccount(targetAccountId);
    if (!targetAccount.deposit(command.money(), sourceAccountId)) {
      accountLock.releaseAccount(sourceAccountId);
      accountLock.releaseAccount(targetAccountId);
      return false;
    }

    updateAccountStatePort.updateActivities(sourceAccount);
    updateAccountStatePort.updateActivities(targetAccount);

    accountLock.releaseAccount(sourceAccountId);
    accountLock.releaseAccount(targetAccountId);
    // 回傳輸出結果
    return true;
  }

  private void checkThreshold(SendMoneyCommand command) {
    if (command.money().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())) {
      throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(),
          command.money());
    }
  }
}
