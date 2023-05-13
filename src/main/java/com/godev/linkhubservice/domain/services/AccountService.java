package com.godev.linkhubservice.domain.services;

import com.godev.linkhubservice.domain.vo.AccountRequest;
import com.godev.linkhubservice.domain.vo.AccountResponse;

public interface AccountService {

    AccountResponse register(AccountRequest accountRequest);

}
