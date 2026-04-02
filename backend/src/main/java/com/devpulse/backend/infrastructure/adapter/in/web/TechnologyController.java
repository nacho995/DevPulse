package com.devpulse.backend.infrastructure.adapter.in.web;

import com.devpulse.backend.domain.model.Technology;
import com.devpulse.backend.domain.port.in.TechnologyUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyUseCase technologyUseCase;
    public TechnologyController(TechnologyUseCase technologyUseCase)
    {
        this.technologyUseCase = technologyUseCase;
    }

    @GetMapping
    public List<Technology> getTechnologies()
    {
        return technologyUseCase.findTechnologies();
    }
    @GetMapping("/{id}")
    public Technology getTechnologyById(@PathVariable Long id){
        return technologyUseCase.findTechnologyById(id).orElseThrow();
    }
    @GetMapping("/type/{type}")
    public List<Technology> getTechnologiesByType(@PathVariable String type){
        return technologyUseCase.findByType(type);
    }

    @PostMapping
    public Technology createTechnology(@RequestBody Technology technology){
        return technologyUseCase.save(technology);
    }

}
