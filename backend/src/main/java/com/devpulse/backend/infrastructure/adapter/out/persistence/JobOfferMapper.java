package com.devpulse.backend.infrastructure.adapter.out.persistence;

import com.devpulse.backend.domain.model.JobOffer;
import org.springframework.stereotype.Component;

@Component
public class JobOfferMapper
{
    public JobOfferEntity toEntity(JobOffer jobOffer)
    {
        JobOfferEntity entity = new JobOfferEntity();
        entity.setId(jobOffer.getId());
        entity.setCompany(jobOffer.getCompany());
        entity.setLocation(jobOffer.getLocation());
        entity.setTitle(jobOffer.getTitle());
        entity.setSource(jobOffer.getSource());
        entity.setUrl(jobOffer.getUrl());
        entity.setModality(jobOffer.getModality());
        entity.setPublishedAt(jobOffer.getPublishedAt());
        entity.setCreatedAt(jobOffer.getCreatedAt());
        return entity;
    }
    public JobOffer toDomain(JobOfferEntity jobOfferEntity)
    {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(jobOfferEntity.getId());
        jobOffer.setCompany(jobOfferEntity.getCompany());
        jobOffer.setLocation(jobOfferEntity.getLocation());
        jobOffer.setTitle(jobOfferEntity.getTitle());
        jobOffer.setSource(jobOfferEntity.getSource());
        jobOffer.setUrl(jobOfferEntity.getUrl());
        jobOffer.setModality(jobOfferEntity.getModality());
        jobOffer.setPublishedAt(jobOfferEntity.getPublishedAt());
        jobOffer.setCreatedAt(jobOfferEntity.getCreatedAt());
        return jobOffer;
    }
}
