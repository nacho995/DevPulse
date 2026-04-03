package com.devpulse.backend.infrastructure.adapter.out.persistence;

import com.devpulse.backend.domain.model.JobOffer;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JobOfferMapperTest {

    private final JobOfferMapper mapper = new JobOfferMapper();

    @Test
    void toDomain_debeConvertirCorrectamente() {
        JobOfferEntity entity = new JobOfferEntity();
        entity.setId(1L);
        entity.setTitle("Java Developer");
        entity.setCompany("Oracle");
        entity.setLocation("Madrid");
        entity.setSource("InfoJobs");
        entity.setUrl("https://infojobs.net/java-dev");
        entity.setModality("remote");
        entity.setPublishedAt(LocalDateTime.of(2026, 4, 1, 10, 0));
        entity.setCreatedAt(LocalDateTime.of(2026, 4, 2, 12, 0));

        JobOffer result = mapper.toDomain(entity);

        assertEquals(1L, result.getId());
        assertEquals("Java Developer", result.getTitle());
        assertEquals("Oracle", result.getCompany());
        assertEquals("Madrid", result.getLocation());
        assertEquals("InfoJobs", result.getSource());
        assertEquals("https://infojobs.net/java-dev", result.getUrl());
        assertEquals("remote", result.getModality());
        assertEquals(LocalDateTime.of(2026, 4, 1, 10, 0), result.getPublishedAt());
        assertEquals(LocalDateTime.of(2026, 4, 2, 12, 0), result.getCreatedAt());
    }

    @Test
    void toEntity_debeConvertirCorrectamente() {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(1L);
        jobOffer.setTitle("Java Developer");
        jobOffer.setCompany("Oracle");
        jobOffer.setLocation("Madrid");
        jobOffer.setSource("InfoJobs");
        jobOffer.setUrl("https://infojobs.net/java-dev");
        jobOffer.setModality("remote");
        jobOffer.setPublishedAt(LocalDateTime.of(2026, 4, 1, 10, 0));
        jobOffer.setCreatedAt(LocalDateTime.of(2026, 4, 2, 12, 0));

        JobOfferEntity entity = mapper.toEntity(jobOffer);

        assertEquals(1L, entity.getId());
        assertEquals("Java Developer", entity.getTitle());
        assertEquals("Oracle", entity.getCompany());
        assertEquals("Madrid", entity.getLocation());
        assertEquals("InfoJobs", entity.getSource());
        assertEquals("https://infojobs.net/java-dev", entity.getUrl());
        assertEquals("remote", entity.getModality());
        assertEquals(LocalDateTime.of(2026, 4, 1, 10, 0), entity.getPublishedAt());
        assertEquals(LocalDateTime.of(2026, 4, 2, 12, 0), entity.getCreatedAt());
    }
}
