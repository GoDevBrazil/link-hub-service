package com.godev.linkhubservice.services;

import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.vo.*;
import org.springframework.security.core.userdetails.UserDetails;

public interface AccountService {

    AccountResponse register(AccountRequest accountRequest);

    UserDetails auth(AuthRequest authRequest);

    Account findByEmail(String email);

    UpdateAccountResponse update(UpdateAccountRequest updateAccountRequest);
}
