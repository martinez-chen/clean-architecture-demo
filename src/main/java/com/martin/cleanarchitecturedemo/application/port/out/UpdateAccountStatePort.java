package com.martin.cleanarchitecturedemo.application.port.out;

import com.martin.cleanarchitecturedemo.application.domain.model.Account;

public interface UpdateAccountStatePort {

  void updateActivities(Account account);
}
