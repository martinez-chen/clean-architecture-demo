package com.martin.cleanarchitecturedemo.application.port.in;

import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PositiveMoneyValidator implements ConstraintValidator<PositiveMoney, Money> {

  @Override
  public boolean isValid(Money money, ConstraintValidatorContext constraintValidatorContext) {
    return money.isPositive();
  }
}
