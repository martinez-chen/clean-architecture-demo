package com.martin.cleanarchitecturedemo.application.port.out;

import com.martin.cleanarchitecturedemo.application.domain.model.Account;

/**
 * 更新帳戶狀態的輸出轉接器。
 */
public interface UpdateAccountStatePort {

  void updateActivities(Account account);
}
