package com.martin.cleanarchitecturedemo.application.port.out;

import com.martin.cleanarchitecturedemo.application.domain.model.Account;
import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import java.time.LocalDateTime;

public interface LoadAccountPort {

  Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
