package com.martin.cleanarchitecturedemo.application.port.out;


import com.martin.cleanarchitecturedemo.application.domain.model.Account;

/**
 * This interface represents the port out for locking and releasing accounts.
 * <p>
 * 翻譯：這個介面代表了鎖定和釋放帳戶的輸出埠。
 */
public interface AccountLock {

	void lockAccount(Account.AccountId accountId);

	void releaseAccount(Account.AccountId accountId);

}
