package com.aiden.dev.footballmatcher.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[a-z0-9_-]{3,20}$")
    private String loginId;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @NotBlank
    @Length(min = 8, max = 20)
    private String passwordConfirm;

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣A-Za-z0-9_-]{3,20}$")
    private String nickname;

    @Email
    @NotBlank
    private String email;

    private String profileImage;
}
