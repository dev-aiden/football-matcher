package com.aiden.dev.footballmatcher.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Length(min = 4, max = 20)
    @Pattern(regexp = "^[a-z0-9_-]{4,20}$")
    private String loginId;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @NotBlank
    @Length(min = 8, max = 20)
    private String passwordConfirm;

    @NotBlank
    @Length(max = 10)
    private String name;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣A-Za-z0-9]{2,10}$")
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$")
    private String phoneNumber;

    @Email
    @NotBlank
    private String email;

    private String profileImage;
}
