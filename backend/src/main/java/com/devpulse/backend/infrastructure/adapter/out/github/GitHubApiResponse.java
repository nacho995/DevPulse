package com.devpulse.backend.infrastructure.adapter.out.github;

public class GitHubApiResponse
{
    private int repositoryCount;
    private int forkCount;
    private int stars;

    public int getRepositoryCount() {
        return repositoryCount;
    }

    public void setRepositoryCount(int repositoryCount) {
        this.repositoryCount = repositoryCount;
    }

    public int getForkCount() {
        return forkCount;
    }

    public void setForkCount(int forkCount) {
        this.forkCount = forkCount;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
