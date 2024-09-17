package com.martin.cleanarchitecturedemo.adapter.in.web;

import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import com.martin.cleanarchitecturedemo.application.port.in.SendMoneyCommand;
import com.martin.cleanarchitecturedemo.application.port.in.SendMoneyUseCase;
import com.martin.cleanarchitecturedemo.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 匯款控制器
 */
@WebAdapter
@RestController
@RequiredArgsConstructor
public class SendMoneyController {

  private final SendMoneyUseCase sendMoneyUseCase;

  /**
   * 匯款 API
   *
   * @param sourceAccountId 來源帳戶 ID
   * @param targetAccountId 目標帳戶 ID
   * @param amount          金額
   */
  @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
  void sendMoney(
      @PathVariable("sourceAccountId") Long sourceAccountId,
      @PathVariable("targetAccountId") Long targetAccountId,
      @PathVariable("amount") Long amount) {
    SendMoneyCommand command = new SendMoneyCommand(
        new AccountId(sourceAccountId),
        new AccountId(targetAccountId),
        Money.of(amount));
    sendMoneyUseCase.sendMoney(command);
  }
}
