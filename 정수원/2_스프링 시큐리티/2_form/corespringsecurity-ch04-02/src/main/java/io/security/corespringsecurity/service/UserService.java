package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void createUser(Account account);

}
