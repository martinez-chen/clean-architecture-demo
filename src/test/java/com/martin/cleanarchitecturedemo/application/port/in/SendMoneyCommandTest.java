package com.martin.cleanarchitecturedemo.application.port.in;

import com.martin.cleanarchitecturedemo.application.domain.model.Account;
import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import jakarta.validation.ConstraintViolationException;
import java.math.BigInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 測試 SendMoneyCommand 的驗證
 */
class SendMoneyCommandTest {

    @Test
    public void validationOk() {
        new SendMoneyCommand(
                new Account.AccountId(42L),
                new Account.AccountId(43L),
                new Money(new BigInteger("10")));
        // no exception
    }

    @Test
    public void moneyValidationFails() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            new SendMoneyCommand(
                    new Account.AccountId(42L),
                    new Account.AccountId(43L),
                    new Money(new BigInteger("-10")));
        });
    }

    @Test
    public void accountIdValidationFails() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            new SendMoneyCommand(
                    new Account.AccountId(42L),
                    null,
                    new Money(new BigInteger("10")));
        });
    }

}