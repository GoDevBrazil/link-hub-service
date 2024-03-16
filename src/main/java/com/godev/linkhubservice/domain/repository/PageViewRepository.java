package com.godev.linkhubservice.domain.repository;

import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.models.PageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface PageViewRepository extends JpaRepository<PageView, Integer> {

    Optional<PageView> findByPageIdAndDate(Page page, OffsetDateTime date);
}
