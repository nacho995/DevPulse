package com.devpulse.backend.infrastructure.adapter.in.web;

import com.devpulse.backend.domain.model.Technology;
import com.devpulse.backend.domain.port.in.TechnologyUseCase;
import com.devpulse.backend.infrastructure.adapter.in.web.dto.TechnologyRequest;
import com.devpulse.backend.infrastructure.adapter.in.web.dto.TechnologyResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.devpulse.backend.infrastructure.adapter.in.web.exception.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyUseCase technologyUseCase;

    public TechnologyController(TechnologyUseCase technologyUseCase) {
        this.technologyUseCase = technologyUseCase;
    }

    @GetMapping
    public List<TechnologyResponse> getTechnologies() {
        return technologyUseCase.findTechnologies().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public TechnologyResponse getTechnologyById(@PathVariable Long id) {
        return toResponse(technologyUseCase.findTechnologyById(id).orElseThrow(() -> new ResourceNotFoundException("Technology not found")));
    }

    @GetMapping("/type/{type}")
    public List<TechnologyResponse> getTechnologiesByType(@PathVariable String type) {
        return technologyUseCase.findByType(type).stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public TechnologyResponse createTechnology(@Valid @RequestBody TechnologyRequest request) {
        Technology technology = new Technology();
        technology.setName(request.getName());
        technology.setType(request.getType());

        Technology saved = technologyUseCase.save(technology);
        return toResponse(saved);
    }

    private TechnologyResponse toResponse(Technology tech) {
        TechnologyResponse response = new TechnologyResponse();
        response.setId(tech.getId());
        response.setName(tech.getName());
        response.setType(tech.getType());
        response.setCreatedAt(tech.getCreatedAt());
        return response;
    }
}
