package com.godev.linkhubservice.services;

import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import com.godev.linkhubservice.domain.vo.UpdatePageRequest;

import java.util.List;

public interface PageService {

    PageResponse create(CreatePageRequest createPageRequest);

    PageResponse update(UpdatePageRequest updatePageRequest, Integer id);

    Page findById(Integer id);

    List<PageResponse> findPagesByAccountId();
}
