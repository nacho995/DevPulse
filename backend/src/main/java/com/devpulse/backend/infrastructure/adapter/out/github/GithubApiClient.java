package com.devpulse.backend.infrastructure.adapter.out.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class GithubApiClient {

    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(GithubApiClient.class);
    public GithubApiClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .build();
    }

    @SuppressWarnings("unchecked")
    public GitHubApiResponse fetchData(String technologyName) {
        Map<String, Object> response = restClient.get()
                .uri("/search/repositories?q=language:{name}&sort=stars&per_page=5", technologyName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        log.info("Calling GitHub API for language: {}", technologyName);
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
