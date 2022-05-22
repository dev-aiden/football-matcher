package com.aiden.dev.footballmatcher.modules.account.validator;

import com.aiden.dev.footballmatcher.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsAccountByLoginId(String loginId);

    boolean existsAccountByNickname(String nickname);

    boolean existsAccountByPhoneNumber(String phoneNumber);

    boolean existsAccountByEmail(String email);
}
