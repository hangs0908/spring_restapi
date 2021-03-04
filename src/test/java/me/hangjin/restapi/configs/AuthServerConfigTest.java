package me.hangjin.restapi.configs;

import me.hangjin.restapi.accounts.Account;
import me.hangjin.restapi.accounts.AccountRole;
import me.hangjin.restapi.accounts.AccountService;
import me.hangjin.restapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @DisplayName("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        //given
//        String clientId = "myApp"; // 우리가 만들고 있는 Application ID
//        String clientSecret = "pass"; // 그에 대한 비밀번호
//
//        String username = "hangjin@email.com";
//        String password = "hangjin";

//        Account hangjin = Account.builder()   // application RUnner에 의해 실행될때 생성되기 때문에 불필요
//                .email(appProperties.getUserUsername())
//                .password(appProperties.getUserPassword())
//                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
//                .build();
//
//        this.accountService.saveAccount(hangjin);
        //when

        //then
        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret())) //clientId와 ClientSecret을 가지고Basic Oauth라는 헤더를 만든것
                    .param("username", appProperties.getUserUsername()) // 파라미터로 grant type , username, password를 줘야함
                       .param("password", appProperties.getUserPassword())
                    .param("grant_type","password")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }


}