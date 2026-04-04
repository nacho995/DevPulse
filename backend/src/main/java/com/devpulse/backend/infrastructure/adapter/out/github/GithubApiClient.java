package com.devpulse.backend.infrastructure.adapter.out.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
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

        String query = "language".equals(type)
                ? "language:" + technologyName
                : "topic:" + technologyName.toLowerCase().replace(" ", "-").replace(".", "");

        Map<String, Object> response = restClient.get()
                .uri("/search/repositories?q={query}&sort=stars&per_page=10", query)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        int totalRepos = (int) response.get("total_count");
        int totalStars = 0;
        int totalForks = 0;
        List<GitHubApiResponse.RepoInfo> topRepos = new ArrayList<>();

        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
        for (Map<String, Object> repo : items) {
            int repoStars = (int) repo.get("stargazers_count");
            int repoForks = (int) repo.get("forks_count");
            totalStars += repoStars;
            totalForks += repoForks;

            GitHubApiResponse.RepoInfo info = new GitHubApiResponse.RepoInfo();
            info.setName((String) repo.get("name"));
            info.setFullName((String) repo.get("full_name"));
            info.setDescription((String) repo.get("description"));
            info.setUrl((String) repo.get("html_url"));
            info.setStars(repoStars);
            info.setForks(repoForks);
            info.setLanguage((String) repo.get("language"));
            topRepos.add(info);
        }

        GitHubApiResponse result = new GitHubApiResponse();
        result.setRepositoryCount(totalRepos);
        result.setStars(totalStars);
        result.setForkCount(totalForks);
        result.setTopRepos(topRepos);
        log.info("GitHub response for {}: {} repos, {} stars", technologyName, totalRepos, totalStars);
        return result;
    }
}
