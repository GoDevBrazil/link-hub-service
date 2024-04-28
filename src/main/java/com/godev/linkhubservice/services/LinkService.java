package com.godev.linkhubservice.services;

import com.godev.linkhubservice.domain.vo.LinkRequest;
import com.godev.linkhubservice.domain.vo.LinkResponse;

public interface LinkService {

    LinkResponse create(LinkRequest linkRequest);
}
