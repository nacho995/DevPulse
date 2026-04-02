package com.devpulse.backend.application.service;

import com.devpulse.backend.domain.model.JobOffer;
import com.devpulse.backend.domain.port.in.JobOfferUseCase;
import com.devpulse.backend.domain.port.out.JobOfferRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobOfferService implements JobOfferUseCase
{
    private final JobOfferRepositoryPort repositoryPort;
    public JobOfferService(JobOfferRepositoryPort repositoryPort)
    {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public List<JobOffer> findAllOffers() {
        return repositoryPort.findAll();
    }

    @Override
    public List<JobOffer> findOfferByTechnology(List<Long> technologyIds) {
        return repositoryPort.findOfferByTechnology(technologyIds);
    }

    @Override
    public Optional<JobOffer> findOfferById(Long id) {
        return repositoryPort.findById(id);
    }
}
