package com.devpulse.backend.infrastructure.adapter.out.persistence;

import com.devpulse.backend.domain.model.Technology;
import org.springframework.stereotype.Component;

@Component
public class TechnologyMapper
{
    public TechnologyEntity toEntity(Technology technology){
        TechnologyEntity entity = new TechnologyEntity();
        entity.setId(technology.getId());
        entity.setName(technology.getName());
        entity.setType(technology.getType());
        entity.setCreatedAt(technology.getCreatedAt());
        return entity;
    }
    public Technology toDomain(TechnologyEntity entity){
        Technology technology = new Technology();
        technology.setId(entity.getId());
        technology.setName(entity.getName());
        technology.setType(entity.getType());
        technology.setCreatedAt(entity.getCreatedAt());
        return technology;
    }
}
