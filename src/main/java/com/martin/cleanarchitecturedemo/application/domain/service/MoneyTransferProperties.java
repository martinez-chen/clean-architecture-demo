package com.martin.cleanarchitecturedemo.application.domain.service;

import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferProperties {

  private Money maximumTransferThreshold = Money.of(1_000_000L);
}
