package com.devpulse.backend.infrastructure.adapter.in.web;

import com.devpulse.backend.domain.model.GithubData;
import com.devpulse.backend.domain.port.in.GithubDataUseCase;
import com.devpulse.backend.infrastructure.adapter.in.scheduler.GithubScheduler;
import com.devpulse.backend.infrastructure.adapter.in.web.dto.GithubDataResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github-data")
public class GithubDataController {

    private final GithubDataUseCase githubDataUseCase;
    private final GithubScheduler githubScheduler;

    public GithubDataController(GithubDataUseCase githubDataUseCase, GithubScheduler githubScheduler) {
        this.githubDataUseCase = githubDataUseCase;
        this.githubScheduler = githubScheduler;
    }

    @GetMapping
    public List<GithubDataResponse> getAll() {
        return githubDataUseCase.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/technologies/{id}")
    public List<GithubDataResponse> getRepositoriesByTechnologyId(@PathVariable Long id) {
        return githubDataUseCase.findRepositoriesByTechnologyId(id).stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/stars-ratio/{id}")
    public Double getStarsRatio(@PathVariable Long id) {
        return githubDataUseCase.starsRationByTechnologyId(id);
    }

    @PostMapping("/fetch")
    public String fetchGithubData() {
        githubScheduler.fetchGithubData();
        return "Github data fetching successfully scheduled";
    }

    private GithubDataResponse toResponse(GithubData data) {
        GithubDataResponse response = new GithubDataResponse();
        response.setId(data.getId());
        response.setTechnologyId(data.getTechnologyId());
        response.setStars(data.getStars());
        response.setForks(data.getForks());
        response.setRepos(data.getRepos());
        response.setCreatedAt(data.getCreatedAt());
        return response;
    }
}
