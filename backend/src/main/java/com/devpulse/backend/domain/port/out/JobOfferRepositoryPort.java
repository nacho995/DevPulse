package com.devpulse.backend.domain.port.out;

import com.devpulse.backend.domain.model.JobOffer;

import java.util.List;
import java.util.Optional;

public interface JobOfferRepositoryPort
{
    List<JobOffer> findAll();
    Optional<JobOffer> findById(Long id);
    List<JobOffer> findOfferByTechnology(List<Long> technologyIds);
    void save(JobOffer jobOffer);
    void deleteById(Long id);
}
