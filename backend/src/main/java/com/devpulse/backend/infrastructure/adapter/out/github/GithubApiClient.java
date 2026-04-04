package com.devpulse.backend.infrastructure.adapter.out.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class GithubApiClient {

    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(GithubApiClient.class);

    public GithubApiClient(@Value("${github.token:}") String githubToken) {
        var builder = RestClient.builder().baseUrl("https://api.github.com");
        if (githubToken != null && !githubToken.isEmpty()) {
            builder.defaultHeader("Authorization", "Bearer " + githubToken);
            log.info("GitHub API client initialized with authentication (5000 req/hour)");
        } else {
            log.warn("GitHub API client initialized WITHOUT authentication (10 req/min)");
        }
        this.restClient = builder.build();
    }

    @SuppressWarnings("unchecked")
    public GitHubApiResponse fetchData(String technologyName, String type) {
        log.info("Calling GitHub API for {}: {}", type, technologyName);

        // Languages use language: filter, frameworks/databases use topic: filter
        String query;
        if ("language".equals(type)) {
            query = "language:" + technologyName;
        } else {
            query = "topic:" + technologyName.toLowerCase().replace(" ", "-").replace(".", "");
        }

        Map<String, Object> response = restClient.get()
                .uri("/search/repositories?q={query}&sort=stars&per_page=5", query)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        int totalRepos = (int) response.get("total_count");

        int totalStars = 0;
        int totalForks = 0;
        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
        for (Map<String, Object> repo : items) {
            totalStars += (int) repo.get("stargazers_count");
            totalForks += (int) repo.get("forks_count");
        }

        GitHubApiResponse result = new GitHubApiResponse();
        result.setRepositoryCount(totalRepos);
        result.setStars(totalStars);
        result.setForkCount(totalForks);
        log.info("GitHub response for {}: {} repos, {} stars, {} forks", technologyName, totalRepos, totalStars, totalForks);
        return result;
    }
}
