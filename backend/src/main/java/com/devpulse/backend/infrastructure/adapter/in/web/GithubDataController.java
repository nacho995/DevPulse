package com.devpulse.backend.infrastructure.adapter.in.web;

import com.devpulse.backend.domain.model.GithubData;
import com.devpulse.backend.domain.port.in.GithubDataUseCase;
import com.devpulse.backend.infrastructure.adapter.in.scheduler.GithubScheduler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github-data")
public class GithubDataController {

    private final GithubDataUseCase githubDataUseCase;
    private GithubScheduler githubScheduler;
    public GithubDataController(GithubDataUseCase githubDataUseCase, GithubScheduler githubScheduler)
    {
        this.githubDataUseCase = githubDataUseCase;
        this.githubScheduler = githubScheduler;
    }

    @GetMapping
    public List<GithubData> getAll() {
        return githubDataUseCase.findAll();
    }

    @GetMapping("/technologies/{id}")
    public List<GithubData> getRepositoriesByTechnologyId(@PathVariable Long id)
    {
        return githubDataUseCase.findRepositoriesByTechnologyId(id);
    }
    @GetMapping("/stars-ratio/{id}")
    public Double getStarsRatio(@PathVariable Long id){
        return githubDataUseCase.starsRationByTechnologyId(id);
    }
    @PostMapping("/fetch")
    public String fetchGithubData() {
       githubScheduler.fetchGithubData();
       return "Github data fetching successfully scheduled";
    }
}
