package com.devpulse.backend.infrastructure.adapter.out.jobapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
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
            log.info("Fetching job offers for: {}", technologyName);

            // Fetch pages of jobs and filter by technology name in title or content
            Map<String, Object> response = restClient.get()
                    .uri("/jobs?page=0&results_per_page=100")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            String searchTerm = technologyName.toLowerCase();

            List<JobApiResponse.JobSummary> matchingJobs = new ArrayList<>();
            for (Map<String, Object> job : results) {
                String name = ((String) job.getOrDefault("name", "")).toLowerCase();
                String contents = ((String) job.getOrDefault("contents", "")).toLowerCase();

                if (name.contains(searchTerm) || contents.contains(searchTerm)) {
                    JobApiResponse.JobSummary summary = new JobApiResponse.JobSummary();
                    summary.setTitle((String) job.get("name"));
                    Map<String, Object> company = (Map<String, Object>) job.get("company");
                    summary.setCompany(company != null ? (String) company.get("name") : "Unknown");
                    List<Map<String, Object>> locations = (List<Map<String, Object>>) job.get("locations");
                    summary.setLocation(locations != null && !locations.isEmpty() ? (String) locations.get(0).get("name") : "Remote");
                    Map<String, Object> refs = (Map<String, Object>) job.get("refs");
                    summary.setUrl(refs != null ? (String) refs.get("landing_page") : "");
                    matchingJobs.add(summary);
                }
            }

            JobApiResponse result = new JobApiResponse();
            result.setTotalOffers(((Number) response.get("total")).intValue());
            result.setMatchingOffers(matchingJobs.size());
            result.setJobs(matchingJobs);
            log.info("Found {} matching jobs for '{}'", matchingJobs.size(), technologyName);
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
