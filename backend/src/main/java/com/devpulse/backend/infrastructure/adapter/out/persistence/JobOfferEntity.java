package com.devpulse.backend.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class JobOfferEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String company;
    private String location;
    private String source;
    private String url;
    private String modality;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "offer_technologies", joinColumns = @JoinColumn(name = "offer_id"), inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private List<TechnologyEntity> technologies;

    public List<TechnologyEntity> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<TechnologyEntity> technologies) {
        this.technologies = technologies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
