package com.devpulse.backend.domain.port.out;

import com.devpulse.backend.domain.model.Technology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface TechnologyRepositoryPort {
    Technology save(Technology technology);
    Optional<Technology> findById(Long id);
    List<Technology> findAll();
    List<Technology> findByType(String type);
    void deleteById(Long id);
    Page<Technology> findAll(Pageable pageable);
}
