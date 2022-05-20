package com.aiden.dev.footballmatcher.modules.teammember;

import com.aiden.dev.footballmatcher.modules.account.Account;
import com.aiden.dev.footballmatcher.modules.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder @AllArgsConstructor @NoArgsConstructor(access = PROTECTED)
public class TeamMember {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_member_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime updatedAt;
}
