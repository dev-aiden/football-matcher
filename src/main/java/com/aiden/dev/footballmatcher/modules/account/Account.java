package com.aiden.dev.footballmatcher.modules.account;

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

    @Column(nullable = false, length = 20)
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

    public void generateEmailCheckToken() {
        this.emailVerified = false;
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }
}
