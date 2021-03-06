package com.aiden.dev.footballmatcher.modules.account;

import com.aiden.dev.footballmatcher.modules.account.validator.SignUpFormValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureTestDatabase
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean SignUpFormValidator signUpFormValidator;
    @MockBean AccountService accountService;

    @DisplayName("회원가입 화면 보이는지 테스트")
    @Test
    void signUpForm() throws Exception {
        given(signUpFormValidator.supports(any())).willReturn(true);

        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @DisplayName("회원가입 테스트 - 입력값 에러")
    @Test
    void signUp_has_error() throws Exception {
        given(signUpFormValidator.supports(any())).willReturn(true);

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
        given(signUpFormValidator.supports(any())).willReturn(true);

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

        then(accountService).should().createAccount(any());
        then(accountService).should().login(any());
    }

    @DisplayName("이메일 인증 테스트 - 잘못된 이메일")
    @Test
    void checkEmailToken_wrong_email() throws Exception {
        given(accountService.findAccountByEmail(any())).willReturn(null);

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
        given(accountService.findAccountByEmail(any())).willReturn(mock(Account.class));
        given(accountService.findAccountByEmail(any()).isInvalidToken(any())).willReturn(true);

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
        given(accountService.findAccountByEmail(any())).willReturn(mock(Account.class));
        given(accountService.findAccountByEmail(any()).isInvalidToken(any())).willReturn(false);

        mockMvc.perform(get("/check-email-token")
                        .param("token", "testToken")
                        .param("email", "test@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(view().name("account/checked-email"));

        then(accountService).should().login(any());
    }

    @DisplayName("인증 메일 페이지 테스트 - 비회원")
    @Test
    void checkEmail_non_member() throws Exception {
        mockMvc.perform(get("/check-email"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @WithAccount(loginId = "aiden")
    @DisplayName("인증 메일 페이지 테스트 - 회원")
    @Test
    void checkEmail_member() throws Exception {
        mockMvc.perform(get("/check-email"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("email"))
                .andExpect(view().name("account/check-email"))
                .andExpect(authenticated().withUsername("aiden"));
    }

    @WithAccount(loginId = "aiden")
    @DisplayName("인증 메일 재발송 확인 - 1시간 이내 재발송")
    @Test
    void resendConfirmEmail_before_1_hour() throws Exception {
        mockMvc.perform(get("/resend-confirm-email"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("email"))
                .andExpect(view().name("account/check-email"));
    }

    @WithAccount(loginId = "aiden", minusHoursForEmailCheckToken = 2L)
    @DisplayName("인증 메일 재발송 확인 - 1시간 이후 재발송")
    @Test
    void resendConfirmEmail_after_1_hour() throws Exception {
        mockMvc.perform(get("/resend-confirm-email"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeDoesNotExist("email"))
                .andExpect(view().name("redirect:/"));

        then(accountService).should().sendSignUpConfirmEmail(any());
    }
}