package com.devpulse.backend.domain.port.in;

import com.devpulse.backend.domain.model.GithubData;

import java.util.List;

public interface GithubDataUseCase
{
    List<GithubData> findRepositoriesByTechnologyId(Long id);
    Double starsRationByTechnologyId(Long id);
    List<GithubData> findAll();
}
