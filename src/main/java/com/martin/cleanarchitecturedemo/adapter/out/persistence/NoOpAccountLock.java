package com.martin.cleanarchitecturedemo.adapter.out.persistence;

import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import com.martin.cleanarchitecturedemo.application.port.out.AccountLock;
import org.springframework.stereotype.Component;

@Component
class NoOpAccountLock implements AccountLock {

	@Override
	public void lockAccount(AccountId accountId) {
		// do nothing
	}

	@Override
	public void releaseAccount(AccountId accountId) {
		// do nothing
	}

}
