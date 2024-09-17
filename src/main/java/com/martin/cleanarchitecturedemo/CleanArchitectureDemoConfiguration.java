package com.martin.cleanarchitecturedemo;

import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import com.martin.cleanarchitecturedemo.application.domain.service.MoneyTransferProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <li>{@code @Configuration} 用於標註一個類為 Spring 的配置類，這個類可以包含 Spring Bean 定義，這些 Bean 會被 Spring
 * 容器管理。@Configuration 類可以包含一個或多個 @Bean 方法，這些方法的返回值會被 Spring 作為 Bean 註冊到容器中。
 * <li>{@code @EnableConfigurationProperties} 用來啟用 Spring Boot 中的屬性配置綁定，通常與 @ConfigurationProperties
 * 一起使用。@EnableConfigurationProperties 的作用是自動加載並管理被 @ConfigurationProperties 註解的類。
 */
@Configuration
@EnableConfigurationProperties(CleanArchitectureDemoConfigurationProperties.class)
public class CleanArchitectureDemoConfiguration {


  @Bean
  public MoneyTransferProperties moneyTransferProperties(CleanArchitectureDemoConfigurationProperties cleanArchitectureDemoConfigurationProperties){
    return new MoneyTransferProperties(Money.of(cleanArchitectureDemoConfigurationProperties.getTransferThreshold()));
  }
}
