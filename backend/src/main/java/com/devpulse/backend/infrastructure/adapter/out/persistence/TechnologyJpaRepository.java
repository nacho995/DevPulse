package com.devpulse.backend.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnologyJpaRepository extends JpaRepository<TechnologyEntity, Long> {
    List<TechnologyEntity> findByType(String type);
}
