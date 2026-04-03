package com.devpulse.backend.infrastructure.adapter.out.persistence;

import com.devpulse.backend.domain.model.Technology;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TechnologyMapperTest {

    private final TechnologyMapper mapper = new TechnologyMapper();

    @Test
    void toDomain_debeConvertirCorrectamente() {
        // GIVEN
        TechnologyEntity entity = new TechnologyEntity();
        entity.setId(1L);
        entity.setName("Java");
        entity.setType("language");
        entity.setCreatedAt(LocalDateTime.of(2026, 4, 2, 12, 0));

        // WHEN
        Technology result = mapper.toDomain(entity);

        // THEN
        assertEquals(1L, result.getId());
        assertEquals("Java", result.getName());
        assertEquals("language", result.getType());
        assertEquals(LocalDateTime.of(2026, 4, 2, 12, 0), result.getCreatedAt());
    }

    @Test
    void toEntity_debeConvertirCorrectamente() {
        // GIVEN
        Technology technology = new Technology();
        technology.setId(1L);
        technology.setName("Java");
        technology.setType("language");
        technology.setCreatedAt(LocalDateTime.of(2026, 4, 2, 12, 0));

        // WHEN
        TechnologyEntity entity = mapper.toEntity(technology);

        // THEN
        assertEquals(1L, entity.getId());
        assertEquals("Java", entity.getName());
        assertEquals("language", entity.getType());
        assertEquals(LocalDateTime.of(2026, 4, 2, 12, 0), entity.getCreatedAt());
    }
}
