package com.godev.linkhubservice.domain.repository;

import com.godev.linkhubservice.domain.models.Link;
import com.godev.linkhubservice.domain.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {

    List<Link> findLinksByPageId(Page pageId);
}
