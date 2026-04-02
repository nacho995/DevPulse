package com.devpulse.backend.infrastructure.adapter.in.web;

import com.devpulse.backend.domain.model.JobOffer;
import com.devpulse.backend.domain.port.in.JobOfferUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-offers")
public class JobOfferController {
    private final JobOfferUseCase jobOfferUseCase;
    public JobOfferController(JobOfferUseCase jobOfferUseCase)
    {
        this.jobOfferUseCase = jobOfferUseCase;
    }

    @GetMapping
    public List<JobOffer> getJobOffers()
    {
        return jobOfferUseCase.findAllOffers();
    }
    @GetMapping("/technology")
    public List<JobOffer> getJobOffersByTechnology(@RequestParam List<Long> technologyIds)
    {
        return jobOfferUseCase.findOfferByTechnology(technologyIds);
    }
    @GetMapping("/{id}")
    public JobOffer getJobOfferById(@PathVariable Long id){
        return jobOfferUseCase.findOfferById(id).orElseThrow();
    }

}
