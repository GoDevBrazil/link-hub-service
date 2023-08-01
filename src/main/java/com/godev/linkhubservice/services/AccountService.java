package com.godev.linkhubservice.services;

import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.vo.AccountRequest;
import com.godev.linkhubservice.domain.vo.AccountResponse;
import com.godev.linkhubservice.domain.vo.AuthRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AccountService {

    AccountResponse register(AccountRequest accountRequest);

    UserDetails auth(AuthRequest authRequest);

    Account findByEmail(String email);

    AccountResponse update(AccountRequest accountRequest);
}
