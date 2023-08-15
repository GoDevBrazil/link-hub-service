package com.godev.linkhubservice.services;

import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.vo.AccountRequest;
import com.godev.linkhubservice.domain.vo.AccountResponse;
import com.godev.linkhubservice.domain.vo.AuthRequest;
import com.godev.linkhubservice.domain.vo.UpdateAccountRequest;
import com.godev.linkhubservice.domain.vo.UpdateAccountResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AccountService {

    AccountResponse register(AccountRequest accountRequest);

    UserDetails auth(AuthRequest authRequest);

    Account findByEmail(String email);

    UpdateAccountResponse update(UpdateAccountRequest updateAccountRequest);
}
