package com.devpulse.backend.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "github_repo")
public class GithubRepoEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long technologyId;
    private String name;
    private String fullName;
    @Column(length = 1000)
    private String description;
    private String url;
    private Integer stars;
    private Integer forks;
    private String language;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTechnologyId() { return technologyId; }
    public void setTechnologyId(Long technologyId) { this.technologyId = technologyId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Integer getStars() { return stars; }
    public void setStars(Integer stars) { this.stars = stars; }
    public Integer getForks() { return forks; }
    public void setForks(Integer forks) { this.forks = forks; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
