package com.devpulse.backend.infrastructure.adapter.in.scheduler;

import com.devpulse.backend.domain.model.GithubData;
import com.devpulse.backend.domain.port.out.GithubDataRepositoryPort;
import com.devpulse.backend.domain.port.out.TechnologyRepositoryPort;
import com.devpulse.backend.infrastructure.adapter.out.github.GithubApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GithubScheduler {

    private static final Logger log = LoggerFactory.getLogger(GithubScheduler.class);

    private final TechnologyRepositoryPort technologyPort;
    private final GithubDataRepositoryPort githubDataPort;
    private final GithubApiClient githubApiClient;

    public GithubScheduler(TechnologyRepositoryPort technologyPort,
                           GithubDataRepositoryPort githubDataPort,
                           GithubApiClient githubApiClient) {
        this.technologyPort = technologyPort;
        this.githubDataPort = githubDataPort;
        this.githubApiClient = githubApiClient;
    }

    @Scheduled(fixedRate = 86400000)
    public void fetchGithubData() {
        var technologies = technologyPort.findAll();
        log.info("Starting GitHub data fetch for {} technologies", technologies.size());

        for (var tech : technologies) {
            try {
                log.info("Fetching data for {}", tech.getName());
                var response = githubApiClient.fetchData(tech.getName());

                GithubData data = new GithubData();
                data.setTechnologyId(tech.getId());
                data.setStars(response.getStars());
                data.setForks(response.getForkCount());
                data.setRepos(response.getRepositoryCount());
                data.setCreatedAt(LocalDateTime.now());

                githubDataPort.save(data);
                log.info("Saved data for {}: {} repos, {} stars", tech.getName(), response.getRepositoryCount(), response.getStars());
            } catch (Exception e) {
                log.error("Failed to fetch data for {}: {}", tech.getName(), e.getMessage());
            }
        }

        log.info("GitHub data fetch completed");
    }
}
