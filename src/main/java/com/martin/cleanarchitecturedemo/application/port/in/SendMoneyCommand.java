package com.martin.cleanarchitecturedemo.application.port.in;

import static com.martin.cleanarchitecturedemo.common.validation.Validation.validate;

import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import jakarta.validation.constraints.NotNull;

/**
 * 使用 record 來實作，便能以 immutable 的方式保護這份物件。
 *
 * @param sourceAccountId
 * @param targetAccountId
 * @param money
 */
public record SendMoneyCommand(
    @NotNull AccountId sourceAccountId,
    @NotNull AccountId targetAccountId,
    @NotNull @PositiveMoney Money money) {

  public SendMoneyCommand(
      AccountId sourceAccountId,
      AccountId targetAccountId,
      Money money) {
    this.sourceAccountId = sourceAccountId;
    this.targetAccountId = targetAccountId;
    this.money = money;
    validate(this);
  }

}
