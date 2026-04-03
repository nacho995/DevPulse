package com.devpulse.backend.infrastructure.adapter.out.persistence;

import com.devpulse.backend.domain.model.GithubData;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GithubDataMapperTest {

    private final GithubDataMapper mapper = new GithubDataMapper();

    @Test
    void toDomain_debeConvertirCorrectamente() {
        TechnologyEntity tech = new TechnologyEntity();
        tech.setId(5L);

        GithubDataEntity entity = new GithubDataEntity();
        entity.setId(1L);
        entity.setTechnology(tech);
        entity.setStars(1000);
        entity.setForks(200);
        entity.setRepos(500);
        entity.setCreatedAt(LocalDateTime.of(2026, 4, 2, 12, 0));

        GithubData result = mapper.toDomain(entity);

        assertEquals(1L, result.getId());
        assertEquals(5L, result.getTechnologyId());
        assertEquals(1000, result.getStars());
        assertEquals(200, result.getForks());
        assertEquals(500, result.getRepos());
        assertEquals(LocalDateTime.of(2026, 4, 2, 12, 0), result.getCreatedAt());
    }

    @Test
    void toEntity_debeConvertirCorrectamente() {
        GithubData githubData = new GithubData();
        githubData.setId(1L);
        githubData.setTechnologyId(5L);
        githubData.setStars(1000);
        githubData.setForks(200);
        githubData.setRepos(500);
        githubData.setCreatedAt(LocalDateTime.of(2026, 4, 2, 12, 0));

        GithubDataEntity entity = mapper.toEntity(githubData);

        assertEquals(1L, entity.getId());
        assertEquals(5L, entity.getTechnology().getId());
        assertEquals(1000, entity.getStars());
        assertEquals(200, entity.getForks());
        assertEquals(500, entity.getRepos());
        assertEquals(LocalDateTime.of(2026, 4, 2, 12, 0), entity.getCreatedAt());
    }
}
