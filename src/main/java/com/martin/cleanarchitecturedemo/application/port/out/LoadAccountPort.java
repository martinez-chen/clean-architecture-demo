package com.martin.cleanarchitecturedemo.application.port.out;

import com.martin.cleanarchitecturedemo.application.domain.model.Account;
import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import java.time.LocalDateTime;

/**
 * This interface represents the port out for loading accounts.
 * 翻譯：這個介面代表了載入帳戶的輸出埠。
 */
public interface LoadAccountPort {

  Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
