package com.devpulse.backend.infrastructure.adapter.out.persistence;

import com.devpulse.backend.domain.model.GithubData;

import org.springframework.stereotype.Component;

@Component
public class GithubDataMapper
{
    public GithubDataEntity toEntity(GithubData githubData){
        GithubDataEntity entity = new GithubDataEntity();
        entity.setId(githubData.getId());

        TechnologyEntity technologyEntity = new TechnologyEntity();
        technologyEntity.setId(githubData.getTechnologyId());
        entity.setTechnology(technologyEntity);

        entity.setStars(githubData.getStars());
        entity.setForks(githubData.getForks());
        entity.setRepos(githubData.getRepos());
        entity.setCreatedAt(githubData.getCreatedAt());

        return entity;
    }

    public GithubData toDomain(GithubDataEntity entity){
        GithubData githubData = new GithubData();
        githubData.setId(entity.getId());


        githubData.setTechnologyId(entity.getTechnology().getId());

        githubData.setStars(entity.getStars());
        githubData.setForks(entity.getForks());
        githubData.setRepos(entity.getRepos());
        githubData.setCreatedAt(entity.getCreatedAt());

        return githubData;
    }
}
