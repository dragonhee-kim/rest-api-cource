package me.dragonhee.demoinfleanrestapi.configs;

import me.dragonhee.demoinfleanrestapi.accounts.Account;
import me.dragonhee.demoinfleanrestapi.accounts.AccountRole;
import me.dragonhee.demoinfleanrestapi.accounts.AccountService;
import me.dragonhee.demoinfleanrestapi.common.BaseTestController;
import me.dragonhee.demoinfleanrestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseTestController{
    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰 발급 받는 테스트")
    public void getAuthToken() throws Exception{
        //Given
        String username = "dragonhee.kim2@gmail.com";
        String password = "dydrkfl1";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        this.accountService.saveAccount(account);

        String clientId = "myApp";
        String clientSecret = "pass";

        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId,clientSecret))
                    .param("username",username)
                    .param("password",password)
                    .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())

        ;
    }

}