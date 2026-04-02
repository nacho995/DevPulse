package com.devpulse.backend.domain.port.in;

import com.devpulse.backend.domain.model.Technology;

import java.util.List;
import java.util.Optional;

public interface TechnologyUseCase
{
    List<Technology> findTechnologies();
    List<Technology> findByType(String language);
    Optional<Technology> findTechnologyById(Long id);
    Technology save(Technology technology);
}
