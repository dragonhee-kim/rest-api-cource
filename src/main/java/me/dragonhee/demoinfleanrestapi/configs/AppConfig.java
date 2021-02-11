package me.dragonhee.demoinfleanrestapi.configs;


import me.dragonhee.demoinfleanrestapi.accounts.Account;
import me.dragonhee.demoinfleanrestapi.accounts.AccountRepository;
import me.dragonhee.demoinfleanrestapi.accounts.AccountRole;
import me.dragonhee.demoinfleanrestapi.accounts.AccountService;
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

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account admin = Account.builder()
                        .email("admin@gmail.com")
                        .password("admin")
                        .roles(Set.of(AccountRole.ADMIN))
                        .build();

                accountService.saveAccount(admin);

                Account user = Account.builder()
                        .email("user@gmail.com")
                        .password("user")
                        .roles(Set.of(AccountRole.USER))
                        .build();
                accountService.saveAccount(user);
            }
        };
    }
}
