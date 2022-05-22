package com.aiden.dev.footballmatcher.modules.account.validator;

import com.aiden.dev.footballmatcher.modules.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;

        if(accountRepository.existsAccountByLoginId(signUpForm.getLoginId())) {
            errors.rejectValue("loginId", "invalid.loginId", "이미 사용중인 아이디입니다.");
        }

        if(accountRepository.existsAccountByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", "이미 사용중인 닉네임입니다.");
        }

        if(accountRepository.existsAccountByPhoneNumber(signUpForm.getPhoneNumber())) {
            errors.rejectValue("phoneNumber", "invalid.phoneNumber", "이미 사용중인 핸드폰 번호입니다.");
        }

        if(accountRepository.existsAccountByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", "이미 사용중인 이메일입니다.");
        }

        if(isMismatchedPassword(signUpForm.getPassword(), signUpForm.getPasswordConfirm())) {
            errors.rejectValue("password", "wrong.value", "입력한 비밀번호가 일치하지 않습니다.");
        }
    }

    private boolean isMismatchedPassword(String password, String passwordConfirm) {
        return !password.equals(passwordConfirm);
    }
}
