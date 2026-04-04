package com.devpulse.backend.infrastructure.adapter.in.scheduler;

import com.devpulse.backend.domain.model.GithubData;
import com.devpulse.backend.domain.model.JobOffer;
import com.devpulse.backend.domain.port.out.GithubDataRepositoryPort;
import com.devpulse.backend.domain.port.out.JobOfferRepositoryPort;
import com.devpulse.backend.domain.port.out.TechnologyRepositoryPort;
import com.devpulse.backend.infrastructure.adapter.out.github.GithubApiClient;
import com.devpulse.backend.infrastructure.adapter.out.jobapi.JobApiClient;
import com.devpulse.backend.infrastructure.adapter.out.persistence.GithubRepoEntity;
import com.devpulse.backend.infrastructure.adapter.out.persistence.GithubRepoJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class GithubScheduler {

    private static final Logger log = LoggerFactory.getLogger(GithubScheduler.class);

    private final TechnologyRepositoryPort technologyPort;
    private final GithubDataRepositoryPort githubDataPort;
    private final JobOfferRepositoryPort jobOfferPort;
    private final GithubApiClient githubApiClient;
    private final JobApiClient jobApiClient;
    private final GithubRepoJpaRepository repoRepository;

    public GithubScheduler(TechnologyRepositoryPort technologyPort,
                           GithubDataRepositoryPort githubDataPort,
                           JobOfferRepositoryPort jobOfferPort,
                           GithubApiClient githubApiClient,
                           JobApiClient jobApiClient,
                           GithubRepoJpaRepository repoRepository) {
        this.technologyPort = technologyPort;
        this.githubDataPort = githubDataPort;
        this.jobOfferPort = jobOfferPort;
        this.githubApiClient = githubApiClient;
        this.jobApiClient = jobApiClient;
        this.repoRepository = repoRepository;
    }

    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void fetchGithubData() {
        var technologies = technologyPort.findAll();
        log.info("Starting data fetch for {} technologies", technologies.size());

        for (var tech : technologies) {
            try {
                log.info("Fetching GitHub data for {}", tech.getName());
                var ghResponse = githubApiClient.fetchData(tech.getName(), tech.getType());

                GithubData data = new GithubData();
                data.setTechnologyId(tech.getId());
                data.setStars(ghResponse.getStars());
                data.setForks(ghResponse.getForkCount());
                data.setRepos(ghResponse.getRepositoryCount());
                data.setCreatedAt(LocalDateTime.now());
                githubDataPort.save(data);

                // Save top repos
                repoRepository.deleteByTechnologyId(tech.getId());
                for (var repo : ghResponse.getTopRepos()) {
                    GithubRepoEntity entity = new GithubRepoEntity();
                    entity.setTechnologyId(tech.getId());
                    entity.setName(repo.getName());
                    entity.setFullName(repo.getFullName());
                    entity.setDescription(repo.getDescription() != null && repo.getDescription().length() > 990
                            ? repo.getDescription().substring(0, 990) : repo.getDescription());
                    entity.setUrl(repo.getUrl());
                    entity.setStars(repo.getStars());
                    entity.setForks(repo.getForks());
                    entity.setLanguage(repo.getLanguage());
                    repoRepository.save(entity);
                }
                log.info("Saved {} repos for {}", ghResponse.getTopRepos().size(), tech.getName());

                Thread.sleep(2000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Failed to fetch data for {}: {}", tech.getName(), e.getMessage());
                try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
            }
        }
        log.info("Data fetch completed");
    }
}
