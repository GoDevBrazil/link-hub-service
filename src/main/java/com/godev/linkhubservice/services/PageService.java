package com.godev.linkhubservice.domain.services;

import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;

public interface PageService {

    PageResponse create(CreatePageRequest createPageRequest);
}
