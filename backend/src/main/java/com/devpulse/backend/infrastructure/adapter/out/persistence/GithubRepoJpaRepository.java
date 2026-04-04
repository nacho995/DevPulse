package com.devpulse.backend.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GithubRepoJpaRepository extends JpaRepository<GithubRepoEntity, Long> {
    List<GithubRepoEntity> findByTechnologyId(Long technologyId);
    void deleteByTechnologyId(Long technologyId);
}
