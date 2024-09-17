package com.martin.cleanarchitecturedemo.application.port.in;

import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import jakarta.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @Constraint} 標記的自定義注解，用於驗證金額是否為正數。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositiveMoneyValidator.class)
@Documented
public @interface PositiveMoney {
  String message() default "must be positive" +
      " found: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Money>[] payload() default {};
}
