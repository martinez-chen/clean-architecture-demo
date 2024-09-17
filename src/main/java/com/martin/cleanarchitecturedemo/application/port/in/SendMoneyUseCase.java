package com.martin.cleanarchitecturedemo.application.port.in;

public interface SendMoneyUseCase {

  boolean sendMoney(SendMoneyCommand command);
}
