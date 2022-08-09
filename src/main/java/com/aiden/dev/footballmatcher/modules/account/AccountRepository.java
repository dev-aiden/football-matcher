package com.aiden.dev.footballmatcher.modules.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsAccountByLoginId(String loginId);

    boolean existsAccountByNickname(String nickname);

    boolean existsAccountByPhoneNumber(String phoneNumber);

    boolean existsAccountByEmail(String email);

    Optional<Account> findByLoginId(String loginId);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByNickname(String nickname);
}
