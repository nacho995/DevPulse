package com.devpulse.backend.application.service;

import com.devpulse.backend.domain.model.GithubData;
import com.devpulse.backend.domain.port.in.GithubDataUseCase;
import com.devpulse.backend.domain.port.out.GithubDataRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GithubDataService implements GithubDataUseCase {

    private final GithubDataRepositoryPort repositoryPort;
    public GithubDataService(GithubDataRepositoryPort repositoryPort)
    {
        this.repositoryPort = repositoryPort;
    }
    @Override
    public List<GithubData> findRepositoriesByTechnologyId(Long id) {
        return repositoryPort.findByTechnologyId(id);
    }

    @Override
    public Double starsRationByTechnologyId(Long id) {
        return repositoryPort.findByTechnologyId(id).stream().mapToDouble(GithubData::getStars).average().orElse(0.0);
    }

    @Override
    public List<GithubData> findAll() {
        return repositoryPort.findAll();
    }
}
