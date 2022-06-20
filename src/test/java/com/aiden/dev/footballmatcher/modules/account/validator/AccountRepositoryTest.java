package com.aiden.dev.footballmatcher.modules.account.validator;

import com.aiden.dev.footballmatcher.modules.account.Account;
import com.aiden.dev.footballmatcher.modules.account.AccountRepository;
import com.aiden.dev.footballmatcher.modules.account.form.SignUpForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setLoginId("aiden");
        signUpForm.setPassword("aiden1234");
        signUpForm.setPasswordConfirm("aiden1234");
        signUpForm.setName("aiden");
        signUpForm.setNickname("aiden");
        signUpForm.setPhoneNumber("01011112222");
        signUpForm.setEmail("aiden@email.com");
        accountRepository.save(Account.createAccount(signUpForm));
    }

    @DisplayName("로그인 아이디로 계정 존재여부 확인하는 쿼리 테스트")
    @Test
    void existsAccountByLoginId() {
        assertThat(accountRepository.existsAccountByLoginId("aiden")).isTrue();
        assertThat(accountRepository.existsAccountByLoginId("aiden2")).isFalse();
    }

    @DisplayName("닉네임으로 계정 존재여부 확인하는 쿼리 테스트")
    @Test
    void existsAccountByNickname() {
        assertThat(accountRepository.existsAccountByNickname("aiden")).isTrue();
        assertThat(accountRepository.existsAccountByNickname("aiden2")).isFalse();
    }

    @DisplayName("핸드폰 번호로 계정 존재여부 확인하는 쿼리 테스트")
    @Test
    void existsAccountByPhoneNumber() {
        assertThat(accountRepository.existsAccountByPhoneNumber("01011112222")).isTrue();
        assertThat(accountRepository.existsAccountByPhoneNumber("01000000000")).isFalse();
    }

    @DisplayName("이메일로 계정 존재여부 확인하는 쿼리 테스트")
    @Test
    void existsAccountByEmail() {
        assertThat(accountRepository.existsAccountByEmail("aiden@email.com")).isTrue();
        assertThat(accountRepository.existsAccountByEmail("aiden2@email.com")).isFalse();
    }

    @DisplayName("LoginId로 계정 조회 쿼리 테스트")
    @Test
    void findByLoginId() {
        assertThat(accountRepository.findByLoginId("aiden")).isNotEmpty();
        assertThat(accountRepository.findByLoginId("aiden2")).isEmpty();
    }

    @DisplayName("이메일로 계정 조회 쿼리 테스트")
    @Test
    void findByEmail() {
        assertThat(accountRepository.findByEmail("aiden@email.com")).isNotEmpty();
        assertThat(accountRepository.findByEmail("aiden2@email.com")).isEmpty();
    }
}