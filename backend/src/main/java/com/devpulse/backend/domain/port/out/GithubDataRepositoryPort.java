package com.devpulse.backend.domain.port.out;

import com.devpulse.backend.domain.model.GithubData;

import java.util.List;
import java.util.Optional;

public interface GithubDataRepositoryPort
{
    List<GithubData> findAll();
    GithubData save(GithubData githubData);
    void deleteById(Long id);
    Optional<GithubData> findById(Long id);
    List<GithubData> findByTechnologyId(Long id);
}
