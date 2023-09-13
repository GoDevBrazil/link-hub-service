package com.godev.linkhubservice.domain.repository;

import com.godev.linkhubservice.domain.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    Optional<Page> findBySlug(String slug);

    List<Page> findPagesByAccount_Id(Integer accountId);
}
