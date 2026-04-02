package com.devpulse.backend.application.service;

import com.devpulse.backend.domain.model.Technology;
import com.devpulse.backend.domain.port.in.TechnologyUseCase;
import com.devpulse.backend.domain.port.out.TechnologyRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TechnologyService implements TechnologyUseCase
{
    private final TechnologyRepositoryPort repositoryPort;

    public TechnologyService(TechnologyRepositoryPort repositoryPort)
    {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public List<Technology> findTechnologies() {
        return repositoryPort.findAll();
    }

    @Override
    public List<Technology> findByType(String language) {
        return repositoryPort.findByType(language);
    }

    @Override
    public Optional<Technology> findTechnologyById(Long id) {
        return repositoryPort.findById(id);
    }

    @Override
    public Technology save(Technology technology) {
       return repositoryPort.save(technology);
    }
}
