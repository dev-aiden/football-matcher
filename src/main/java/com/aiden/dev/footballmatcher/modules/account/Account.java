package com.aiden.dev.footballmatcher.modules.account;

import com.aiden.dev.footballmatcher.modules.account.form.SignUpForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder @AllArgsConstructor @NoArgsConstructor(access = PROTECTED)
public class Account {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String nickname;

    @Column(nullable = false, unique = true, length = 11)
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    @Lob
    private String profileImage;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime updatedAt;

    public static Account createAccount(SignUpForm signUpForm) {
        Account account = new Account();
        account.loginId = signUpForm.getLoginId();
        account.password = signUpForm.getPassword();
        account.name = signUpForm.getName();
        account.nickname = signUpForm.getNickname();
        account.email = signUpForm.getEmail();
        account.phoneNumber = signUpForm.getPhoneNumber();
        account.profileImage = signUpForm.getProfileImage();
        account.emailVerified = false;
        account.emailCheckToken = UUID.randomUUID().toString();
        account.emailCheckTokenGeneratedAt = LocalDateTime.now();
        return account;
    }

    public Boolean isInvalidToken(String token) {
        return !this.emailCheckToken.equals(token);
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.createdAt = LocalDateTime.now();
    }
}
