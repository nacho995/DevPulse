package com.devpulse.backend.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobOfferJpaRepository extends JpaRepository<JobOfferEntity, Long> {
    @Query("SELECT j FROM JobOfferEntity j JOIN j.technologies t WHERE t.id IN :technologyIds")
    List<JobOfferEntity> findByTechnologyIds(@Param("technologyIds") List<Long> technologyIds);

    
}
