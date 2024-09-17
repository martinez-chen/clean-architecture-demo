package com.martin.cleanarchitecturedemo.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * 用於標記業務邏輯層的組件。
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface UseCase {

  /**
   * 該值可能表示邏輯組件名稱的建議， 以便在自動檢測到的組件中轉換為Spring bean。
   *
   * @return 建議的組件名稱（如果有的話，否則為空字符串）
   */
  @AliasFor(annotation = Component.class)
  String value() default "";

}
