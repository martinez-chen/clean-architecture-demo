package com.martin.cleanarchitecturedemo.common.validation;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

public class Validation {

  /**
   * IDE可能會提示需要關閉ValidatorFactory，但如果我們在這裡關閉，
   * 就會破壞ValidatorFactory#close的契約。
   */
  private static final Validator validator = buildDefaultValidatorFactory().getValidator();

  // private final static ValidatorFactory validatorFactory = buildDefaultValidatorFactory();
  // private final static Validator validator = validatorFactory.getValidator();
  //
  // static {
  //   Runtime.getRuntime().addShutdownHook(new Thread(validatorFactory::close));
  // }

  /**
   * Evaluates all Bean Validation annotations on the subject.
   * 驗證主體上的所有Bean Validation註解。
   */
  public static <T> void validate(T subject) {
    Set<ConstraintViolation<T>> violations = validator.validate(subject);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
