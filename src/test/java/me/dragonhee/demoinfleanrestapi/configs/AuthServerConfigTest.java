package me.dragonhee.demoinfleanrestapi.configs;

import me.dragonhee.demoinfleanrestapi.accounts.Account;
import me.dragonhee.demoinfleanrestapi.accounts.AccountRole;
import me.dragonhee.demoinfleanrestapi.accounts.AccountService;
import me.dragonhee.demoinfleanrestapi.common.AppProperties;
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

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰 발급 받는 테스트")
    public void getAuthToken() throws Exception{
        //Given

//        Account account = Account.builder()
//                .email(appProperties.getUserUsername())
//                .password(appProperties.getUserPassword())
//                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
//                .build();
//
//        this.accountService.saveAccount(account);


        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret()))
                    .param("username",appProperties.getUserUsername())
                    .param("password",appProperties.getUserPassword())
                    .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())

        ;
    }

}