package com.devpulse.backend.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GithubDataJpaRepository extends JpaRepository<GithubDataEntity, Long>
{
    List<GithubDataEntity> findByTechnologyId(Long technologyId);
}
