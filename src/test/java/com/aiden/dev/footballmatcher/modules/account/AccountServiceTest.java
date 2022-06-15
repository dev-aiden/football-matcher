package com.aiden.dev.footballmatcher.modules.account;

import com.aiden.dev.footballmatcher.infra.mail.EmailService;
import com.aiden.dev.footballmatcher.modules.account.form.SignUpForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks AccountService accountService;
    @Mock EmailService emailService;
    @Mock AccountRepository accountRepository;
    @Spy PasswordEncoder passwordEncoder;

    @DisplayName("계정 생성 테스트")
    @Test
    void createAccount() {
        // given
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setLoginId("aiden");
        signUpForm.setPassword("aiden1234");
        signUpForm.setPasswordConfirm("aiden1234");
        signUpForm.setName("aiden");
        signUpForm.setNickname("aiden");
        signUpForm.setPhoneNumber("01011112222");
        signUpForm.setEmail("aiden@email.com");

        Account account = Account.builder()
                .loginId("aiden")
                .nickname("aiden")
                .email("aiden@email.com")
                .build();

        given(passwordEncoder.encode(any())).willReturn("encryptPassword");
        given(accountRepository.save(any())).willReturn(account);

        // when
        Account savedAccount = accountService.createAccount(signUpForm);

        // then
        then(emailService).should().sendEmail(any());
        assertThat(savedAccount.getLoginId()).isEqualTo(signUpForm.getLoginId());
        assertThat(savedAccount.getPassword()).isNotEqualTo(signUpForm.getPassword());
        assertThat(savedAccount.getNickname()).isEqualTo(signUpForm.getNickname());
        assertThat(savedAccount.getEmail()).isEqualTo(signUpForm.getEmail());
    }

    @DisplayName("이메일로 계정 조회 쿼리 테스트")
    @Test
    void findAccountByEmail() {
        // when
        accountService.findAccountByEmail("aiden@email.com");

        // then
        then(accountRepository).should().findByEmail("aiden@email.com");
    }
}