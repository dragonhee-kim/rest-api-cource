package me.dragonhee.demoinfleanrestapi.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findByUserName(){
        //Given

        String email = "dragonhee.kim@gmail.com";
        String password = "dydrkfl1";

        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN,AccountRole.USER))
                .build();
        accountRepository.save(account);

        // When
        UserDetailsService userDetailsService =  accountService;
        UserDetails userDetails = accountService.loadUserByUsername("dragonhee.kim@gmail.com");

        ///THEN
        assertEquals(userDetails.getPassword(),password);

    }

    @Test//(expected = UsernameNotFoundException.class)
    public void findByUsernameFail(){
        //Expected
        String username = "random@gmail.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        //when
        accountService.loadUserByUsername(username);

//        try{
//            accountService.loadUserByUsername(username);
//            fail("supoosed to be failed");
//        }catch (UsernameNotFoundException e ){
//
//        }
    }


}