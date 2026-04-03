package com.devpulse.backend.infrastructure.adapter.in.web;

import com.devpulse.backend.domain.model.JobOffer;
import com.devpulse.backend.domain.port.in.JobOfferUseCase;
import com.devpulse.backend.infrastructure.adapter.in.web.dto.JobOfferRequest;
import com.devpulse.backend.infrastructure.adapter.in.web.dto.JobOfferResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-offers")
public class JobOfferController {

    private final JobOfferUseCase jobOfferUseCase;

    public JobOfferController(JobOfferUseCase jobOfferUseCase) {
        this.jobOfferUseCase = jobOfferUseCase;
    }

    @GetMapping
    public List<JobOfferResponse> getJobOffers() {
        return jobOfferUseCase.findAllOffers().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/technology")
    public List<JobOfferResponse> getJobOffersByTechnology(@RequestParam List<Long> technologyIds) {
        return jobOfferUseCase.findOfferByTechnology(technologyIds).stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public JobOfferResponse getJobOfferById(@PathVariable Long id) {
        return toResponse(jobOfferUseCase.findOfferById(id).orElseThrow());
    }

    @PostMapping
    public JobOfferResponse createJobOffer(@Valid @RequestBody JobOfferRequest request) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setTitle(request.getTitle());
        jobOffer.setCompany(request.getCompany());
        jobOffer.setLocation(request.getLocation());
        jobOffer.setSource(request.getSource());
        jobOffer.setUrl(request.getUrl());
        jobOffer.setModality(request.getModality());

        return toResponse(jobOffer);
    }

    private JobOfferResponse toResponse(JobOffer offer) {
        JobOfferResponse response = new JobOfferResponse();
        response.setId(offer.getId());
        response.setTitle(offer.getTitle());
        response.setCompany(offer.getCompany());
        response.setLocation(offer.getLocation());
        response.setSource(offer.getSource());
        response.setUrl(offer.getUrl());
        response.setModality(offer.getModality());
        response.setPublishedAt(offer.getPublishedAt());
        response.setCreatedAt(offer.getCreatedAt());
        return response;
    }
}
