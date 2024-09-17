package com.martin.cleanarchitecturedemo.application.port.out;


import com.martin.cleanarchitecturedemo.application.domain.model.Account;

public interface AccountLock {

	void lockAccount(Account.AccountId accountId);

	void releaseAccount(Account.AccountId accountId);

}
