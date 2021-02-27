package me.hangjin.restapi.configs;

import me.hangjin.restapi.accounts.Account;
import me.hangjin.restapi.accounts.AccountRole;
import me.hangjin.restapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean //modelmapper는 공용으로 사용하기 때문에 bean 으로 등록해서 사용
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account account = Account.builder()
                        .email("hangs@gmail.com")
                        .password("1234")
                        .roles(Set.of(AccountRole.ADMIN,AccountRole.USER))
                        .build();
                accountService.saveAccount(account);

            }
        };
    }
}
