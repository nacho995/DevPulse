package com.devpulse.backend.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class GithubDataEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "technology_id")
    private TechnologyEntity technology;

    private Integer stars;
    private Integer forks;
    private Integer repos;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TechnologyEntity getTechnology() {
        return technology;
    }

    public void setTechnology(TechnologyEntity technology) {
        this.technology = technology;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getForks() {
        return forks;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
    }

    public Integer getRepos() {
        return repos;
    }

    public void setRepos(Integer repos) {
        this.repos = repos;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
