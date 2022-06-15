package com.aiden.dev.footballmatcher.modules.account;

import com.aiden.dev.footballmatcher.modules.account.validator.SignUpFormValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean SignUpFormValidator signUpFormValidator;
    @MockBean AccountService accountService;

    @DisplayName("회원가입 화면 보이는지 테스트")
    @Test
    void signUpForm() throws Exception {
        when(signUpFormValidator.supports(any())).thenReturn(true);

        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @DisplayName("회원가입 테스트 - 입력값 에러")
    @Test
    void signUp_has_error() throws Exception {
        when(signUpFormValidator.supports(any())).thenReturn(true);

        mockMvc.perform(post("/sign-up")
                        .param("loginId", "aiden")
                        .param("password", "aiden1234")
                        .param("passwordConfirm", "aiden1234")
                        .param("name", "aiden")
                        .param("nickname", "")
                        .param("phoneNumber", "01011112222")
                        .param("email", "aiden@email.com")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("회원가입 테스트 - 입력값 정상")
    @Test
    void signUp() throws Exception {
        when(signUpFormValidator.supports(any())).thenReturn(true);

        mockMvc.perform(post("/sign-up")
                        .param("loginId", "aiden")
                        .param("password", "aiden1234")
                        .param("passwordConfirm", "aiden1234")
                        .param("name", "aiden")
                        .param("nickname", "aiden")
                        .param("phoneNumber", "01011112222")
                        .param("email", "aiden@email.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("이메일 인증 테스트 - 잘못된 이메일")
    @Test
    void checkEmailToken_wrong_email() throws Exception {
        when(accountService.findAccountByEmail(any())).thenReturn(null);

        mockMvc.perform(get("/check-email-token")
                        .param("token", "testToken")
                        .param("email", "test@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "wrong.email"))
                .andExpect(view().name("account/checked-email"));
    }

    @DisplayName("이메일 인증 테스트 - 잘못된 토큰")
    @Test
    void checkEmailToken_wrong_token() throws Exception {
        when(accountService.findAccountByEmail(any())).thenReturn(mock(Account.class));
        when(accountService.findAccountByEmail(any()).isInvalidToken(any())).thenReturn(true);

        mockMvc.perform(get("/check-email-token")
                        .param("token", "testToken")
                        .param("email", "test@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "wrong.token"))
                .andExpect(view().name("account/checked-email"));
    }

    @DisplayName("이메일 인증 테스트")
    @Test
    void checkEmailToken() throws Exception {
        when(accountService.findAccountByEmail(any())).thenReturn(mock(Account.class));
        when(accountService.findAccountByEmail(any()).isInvalidToken(any())).thenReturn(false);

        mockMvc.perform(get("/check-email-token")
                        .param("token", "testToken")
                        .param("email", "test@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(view().name("account/checked-email"));
    }
}