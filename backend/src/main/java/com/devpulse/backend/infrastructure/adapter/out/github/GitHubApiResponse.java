package com.devpulse.backend.infrastructure.adapter.out.github;

import java.util.List;

public class GitHubApiResponse {
    private int repositoryCount;
    private int forkCount;
    private int stars;
    private List<RepoInfo> topRepos;

    public int getRepositoryCount() { return repositoryCount; }
    public void setRepositoryCount(int repositoryCount) { this.repositoryCount = repositoryCount; }
    public int getForkCount() { return forkCount; }
    public void setForkCount(int forkCount) { this.forkCount = forkCount; }
    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }
    public List<RepoInfo> getTopRepos() { return topRepos; }
    public void setTopRepos(List<RepoInfo> topRepos) { this.topRepos = topRepos; }

    public static class RepoInfo {
        private String name;
        private String fullName;
        private String description;
        private String url;
        private int stars;
        private int forks;
        private String language;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public int getStars() { return stars; }
        public void setStars(int stars) { this.stars = stars; }
        public int getForks() { return forks; }
        public void setForks(int forks) { this.forks = forks; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
    }
}
