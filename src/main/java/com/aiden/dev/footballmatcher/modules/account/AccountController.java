package com.aiden.dev.footballmatcher.modules.account;

import com.aiden.dev.footballmatcher.modules.account.form.SignUpForm;
import com.aiden.dev.footballmatcher.modules.account.validator.CurrentAccount;
import com.aiden.dev.footballmatcher.modules.account.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpForm signUpForm, Errors errors) {
        if(errors.hasErrors()) {
            return "account/sign-up";
        }

        accountService.login(accountService.createAccount(signUpForm));
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountService.findAccountByEmail(email);

        if(account == null) {
            model.addAttribute("error", "wrong.email");
            return "account/checked-email";
        }

        if(account.isInvalidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return "account/checked-email";
        }

        accountService.completeSignUp(account);
        return "account/checked-email";
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentAccount Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentAccount Account account, Model model) {
        if(!account.isPossibleSendConfirmEmail()) {
            model.addAttribute("error", "인증 이메일은 1시간에 한 번만 발송이 가능합니다.");
            model.addAttribute("email", account.getEmail());
            return "account/check-email";
        }
        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/";
    }

    @GetMapping("/profile/id/{loginId}")
    public String viewProfileAccountOwner(@PathVariable String loginId, @CurrentAccount Account account) {
        if(!loginId.equals(account.getLoginId())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        return "redirect:/profile/" + account.getNickname();
    }

    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, @CurrentAccount Account account, Model model) {
        Account accountByNickname = accountService.findAccountByNickname(nickname);
        if(accountByNickname == null) {
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 존재하지 않습니다.");
        }
        model.addAttribute(accountByNickname);
        model.addAttribute("isOwner", accountByNickname.equals(account));
        return "account/profile";
    }
}
