package com.devpulse.backend.infrastructure.adapter.out.jobapi;

import java.util.List;

public class JobApiResponse {
    private int totalOffers;
    private int matchingOffers;
    private List<JobSummary> jobs;

    public int getTotalOffers() { return totalOffers; }
    public void setTotalOffers(int totalOffers) { this.totalOffers = totalOffers; }
    public int getMatchingOffers() { return matchingOffers; }
    public void setMatchingOffers(int matchingOffers) { this.matchingOffers = matchingOffers; }
    public List<JobSummary> getJobs() { return jobs; }
    public void setJobs(List<JobSummary> jobs) { this.jobs = jobs; }

    public static class JobSummary {
        private String title;
        private String company;
        private String location;
        private String url;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}
