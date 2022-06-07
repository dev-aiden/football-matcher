package com.aiden.dev.footballmatcher.modules.account;

import com.aiden.dev.footballmatcher.infra.mail.EmailMessage;
import com.aiden.dev.footballmatcher.infra.mail.EmailService;
import com.aiden.dev.footballmatcher.modules.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    public Account createAccount(SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account newAccount = accountRepository.save(Account.createAccount(signUpForm));
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private void sendSignUpConfirmEmail(Account account) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("Football Matcher 회원가입 인증")
                .message("/check-email-token?token=" + account.getEmailCheckToken() + "&email=" + account.getEmail())
                .build();
        emailService.sendEmail(emailMessage);
    }
}
