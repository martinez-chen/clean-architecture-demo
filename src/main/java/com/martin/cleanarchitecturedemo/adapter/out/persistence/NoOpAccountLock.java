package com.martin.cleanarchitecturedemo.adapter.out.persistence;

import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import com.martin.cleanarchitecturedemo.application.port.out.AccountLock;
import org.springframework.stereotype.Component;

/**
 * No operation implementation of {@link AccountLock}.
 * <p>
 * 若有lockAccount需求，則實作新的AccountLock
 */
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
