package com.devpulse.backend.infrastructure.adapter.out.persistence;

import com.devpulse.backend.domain.model.JobOffer;
import com.devpulse.backend.domain.port.out.JobOfferRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JobOfferPersistanceAdapter implements JobOfferRepositoryPort {

    private final JobOfferJpaRepository jpaRepository;
    private final JobOfferMapper mapper;
    public JobOfferPersistanceAdapter(JobOfferJpaRepository jpaRepository, JobOfferMapper mapper)
    {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<JobOffer> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<JobOffer> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<JobOffer> findOfferByTechnology(List<Long> technologyIds) {
        return jpaRepository.findByTechnologyIds(technologyIds).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void save(JobOffer jobOffer) {
        jpaRepository.save(mapper.toEntity(jobOffer));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
