package com.godev.linkhubservice.services;

import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import com.godev.linkhubservice.domain.vo.UpdatePageRequest;

public interface PageService {

    PageResponse create(CreatePageRequest createPageRequest);

    PageResponse update(UpdatePageRequest updatePageRequest, Integer id);

    public Page findById(Integer id);
}
