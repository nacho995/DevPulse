package com.devpulse.backend.infrastructure.adapter.in.scheduler;

import com.devpulse.backend.domain.model.GithubData;
import com.devpulse.backend.domain.model.JobOffer;
import com.devpulse.backend.domain.port.out.GithubDataRepositoryPort;
import com.devpulse.backend.domain.port.out.JobOfferRepositoryPort;
import com.devpulse.backend.domain.port.out.TechnologyRepositoryPort;
import com.devpulse.backend.infrastructure.adapter.out.github.GithubApiClient;
import com.devpulse.backend.infrastructure.adapter.out.jobapi.JobApiClient;
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
    private final JobOfferRepositoryPort jobOfferPort;
    private final GithubApiClient githubApiClient;
    private final JobApiClient jobApiClient;

    public GithubScheduler(TechnologyRepositoryPort technologyPort,
                           GithubDataRepositoryPort githubDataPort,
                           JobOfferRepositoryPort jobOfferPort,
                           GithubApiClient githubApiClient,
                           JobApiClient jobApiClient) {
        this.technologyPort = technologyPort;
        this.githubDataPort = githubDataPort;
        this.jobOfferPort = jobOfferPort;
        this.githubApiClient = githubApiClient;
        this.jobApiClient = jobApiClient;
    }

    @Scheduled(fixedRate = 86400000)
    public void fetchGithubData() {
        var technologies = technologyPort.findAll();
        log.info("Starting data fetch for {} technologies", technologies.size());

        for (var tech : technologies) {
            try {
                // Fetch GitHub data
                log.info("Fetching GitHub data for {}", tech.getName());
                var ghResponse = githubApiClient.fetchData(tech.getName());

                GithubData data = new GithubData();
                data.setTechnologyId(tech.getId());
                data.setStars(ghResponse.getStars());
                data.setForks(ghResponse.getForkCount());
                data.setRepos(ghResponse.getRepositoryCount());
                data.setCreatedAt(LocalDateTime.now());
                githubDataPort.save(data);
                log.info("Saved GitHub data for {}: {} repos, {} stars", tech.getName(), ghResponse.getRepositoryCount(), ghResponse.getStars());

                // Fetch job offers
                log.info("Fetching job offers for {}", tech.getName());
                var jobResponse = jobApiClient.fetchJobs(tech.getName());

                for (var job : jobResponse.getJobs()) {
                    JobOffer offer = new JobOffer();
                    offer.setTitle(job.getTitle());
                    offer.setCompany(job.getCompany());
                    offer.setLocation(job.getLocation());
                    offer.setUrl(job.getUrl());
                    offer.setSource("The Muse");
                    offer.setModality("onsite");
                    offer.setPublishedAt(LocalDateTime.now());
                    offer.setCreatedAt(LocalDateTime.now());
                    jobOfferPort.save(offer);
                }
                log.info("Saved {} job offers for {}", jobResponse.getJobs().size(), tech.getName());

            } catch (Exception e) {
                log.error("Failed to fetch data for {}: {}", tech.getName(), e.getMessage());
            }
        }

        log.info("Data fetch completed");
    }
}
