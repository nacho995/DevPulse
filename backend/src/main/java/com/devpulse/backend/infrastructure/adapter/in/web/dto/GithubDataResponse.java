package com.devpulse.backend.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;

public class GithubDataResponse {
    private Long id;
    private Long technologyId;
    private Integer stars;
    private Integer forks;
    private Integer repos;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTechnologyId() { return technologyId; }
    public void setTechnologyId(Long technologyId) { this.technologyId = technologyId; }
    public Integer getStars() { return stars; }
    public void setStars(Integer stars) { this.stars = stars; }
    public Integer getForks() { return forks; }
    public void setForks(Integer forks) { this.forks = forks; }
    public Integer getRepos() { return repos; }
    public void setRepos(Integer repos) { this.repos = repos; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
