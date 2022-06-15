package com.aiden.dev.footballmatcher;

import com.aiden.dev.footballmatcher.modules.account.Account;
import com.aiden.dev.footballmatcher.modules.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("통합 테스트")
class IntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        Account account = Account.builder()
                .loginId("aiden")
                .password("aiden1234")
                .name("aiden")
                .nickname("aiden")
                .phoneNumber("01011112222")
                .email("aiden@email.com")
                .emailCheckToken("aidenToken")
                .emailVerified(false)
                .build();
        accountRepository.save(account);
    }

    @DisplayName("index 페이지 테스트")
    @Test
    void home() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 폼 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 테스트 - 잘못된 입력값")
    @Test
    void signUp_wrong_value() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("loginId", "aiden")
                        .param("password", "aiden1234")
                        .param("passwordConfirm", "aiden5678")
                        .param("name", "aiden")
                        .param("nickname", "aiden")
                        .param("phoneNumber", "01011112222")
                        .param("email", "aiden@email.com")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 테스트")
    @Test
    void signUp() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("loginId", "aiden2")
                        .param("password", "aiden1234")
                        .param("passwordConfirm", "aiden1234")
                        .param("name", "aiden2")
                        .param("nickname", "aiden2")
                        .param("phoneNumber", "01022223333")
                        .param("email", "aiden2@email.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());

        assertThat(accountRepository.findByEmail("aiden2@email.com")).isNotNull();
    }

    @DisplayName("이메일 인증 테스트 - 잘못된 이메일")
    @Test
    void checkEmailToken_wrong_email() throws Exception {
        mockMvc.perform(get("/check-email-token")
                        .param("token", "aidenToken")
                        .param("email", "aiden2@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "wrong.email"))
                .andExpect(view().name("account/checked-email"));
    }

    @DisplayName("이메일 인증 테스트 - 잘못된 토큰")
    @Test
    void checkEmailToken_wrong_token() throws Exception {

        mockMvc.perform(get("/check-email-token")
                        .param("token", "aidenToken2")
                        .param("email", "aiden@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "wrong.token"))
                .andExpect(view().name("account/checked-email"));
    }

    @DisplayName("이메일 인증 테스트")
    @Test
    void checkEmailToken() throws Exception {
        mockMvc.perform(get("/check-email-token")
                        .param("token", "aidenToken")
                        .param("email", "aiden@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(view().name("account/checked-email"));

        assertThat(accountRepository.findByEmail("aiden@email.com").get().isEmailVerified()).isTrue();
    }
}
