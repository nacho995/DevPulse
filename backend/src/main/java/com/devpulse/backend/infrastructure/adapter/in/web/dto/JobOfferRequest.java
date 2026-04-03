package com.devpulse.backend.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public class JobOfferRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String company;
    private String location;
    private String source;
    private String url;
    private String modality;

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
}