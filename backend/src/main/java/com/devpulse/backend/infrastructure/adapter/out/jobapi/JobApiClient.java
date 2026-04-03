package com.devpulse.backend.infrastructure.adapter.out.jobapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class JobApiClient {

    private static final Logger log = LoggerFactory.getLogger(JobApiClient.class);
    private final RestClient restClient;

    public JobApiClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://www.themuse.com/api/public")
                .build();
    }

    @SuppressWarnings("unchecked")
    public JobApiResponse fetchJobs(String technologyName) {
        try {
            log.info("Fetching job offers for technology: {}", technologyName);
            Map<String, Object> response = restClient.get()
                    .uri("/jobs?page=0&results_per_page=20&category=Engineering&level=Entry%20Level&level=Mid%20Level&level=Senior%20Level")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            int totalOffers = ((Number) response.get("total")).intValue();
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            // Filter results that mention the technology in name or contents
            long matchingOffers = results.stream()
                    .filter(job -> {
                        String name = ((String) job.getOrDefault("name", "")).toLowerCase();
                        String contents = ((String) job.getOrDefault("contents", "")).toLowerCase();
                        return name.contains(technologyName.toLowerCase()) ||
                               contents.contains(technologyName.toLowerCase());
                    })
                    .count();

            // Also search specifically for the technology
            Map<String, Object> techResponse = restClient.get()
                    .uri("/jobs?page=0&results_per_page=1&category=Engineering")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            JobApiResponse result = new JobApiResponse();
            result.setTotalOffers(totalOffers);
            result.setMatchingOffers((int) matchingOffers);

            // Extract sample jobs
            List<JobApiResponse.JobSummary> jobs = results.stream()
                    .limit(5)
                    .map(job -> {
                        JobApiResponse.JobSummary summary = new JobApiResponse.JobSummary();
                        summary.setTitle((String) job.get("name"));
                        Map<String, Object> company = (Map<String, Object>) job.get("company");
                        summary.setCompany(company != null ? (String) company.get("name") : "Unknown");
                        List<Map<String, Object>> locations = (List<Map<String, Object>>) job.get("locations");
                        summary.setLocation(locations != null && !locations.isEmpty() ? (String) locations.get(0).get("name") : "Remote");
                        Map<String, Object> refs = (Map<String, Object>) job.get("refs");
                        summary.setUrl(refs != null ? (String) refs.get("landing_page") : "");
                        return summary;
                    })
                    .toList();
            result.setJobs(jobs);

            log.info("Found {} total engineering offers, {} matching '{}'", totalOffers, matchingOffers, technologyName);
            return result;
        } catch (Exception e) {
            log.error("Failed to fetch jobs for {}: {}", technologyName, e.getMessage());
            JobApiResponse empty = new JobApiResponse();
            empty.setTotalOffers(0);
            empty.setMatchingOffers(0);
            empty.setJobs(List.of());
            return empty;
        }
    }
}
