package com.devpulse.backend.domain.port.in;

import com.devpulse.backend.domain.model.JobOffer;

import java.util.List;
import java.util.Optional;

public interface JobOfferUseCase
{
    List<JobOffer> findAllOffers();
    List<JobOffer> findOfferByTechnology(List<Long> technologyIds);
    Optional<JobOffer> findOfferById(Long id);
}
