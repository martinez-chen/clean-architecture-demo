package com.martin.cleanarchitecturedemo.adapter.in.web;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.martin.cleanarchitecturedemo.application.domain.model.Account.AccountId;
import com.martin.cleanarchitecturedemo.application.domain.model.Money;
import com.martin.cleanarchitecturedemo.application.port.in.SendMoneyCommand;
import com.martin.cleanarchitecturedemo.application.port.in.SendMoneyUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * SendMoneyControllerTest 只加載與 Web 層相關的 Bean，如控制器、過濾器、攔截器等。
 * <p>
 * 與 MockMvc 一起使用，可以模擬 HTTP 請求並驗證回應是否符合預期。
 */
@WebMvcTest(controllers = SendMoneyController.class)
class SendMoneyControllerTest {

  /**
   * 使用 @Autowired 來注入 MockMvc 實例。
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * 使用 @MockBean 來模擬 SendMoneyUseCase 介面的實現。
   */
  @MockBean
  private SendMoneyUseCase sendMoneyUseCase;

  /**
   * andExcept 表示 HTTP 回應的期望值，用於模擬 HTTP 請求並驗證回應是否符合預期。
   *
   * @throws Exception Exception
   */
  @Test
  void testSendMoney() throws Exception {
    mockMvc.perform(post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
            40L, 41L, 500)
            .header("Content-Type", "application/json"))
        .andExpect(status().isOk());

    then(sendMoneyUseCase).should()
        .sendMoney(eq(new SendMoneyCommand(
            new AccountId(40L),
            new AccountId(41L),
            Money.of(500L))));
  }

}