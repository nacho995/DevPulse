package com.devpulse.backend.infrastructure.adapter.out.persistence;

import com.devpulse.backend.domain.model.Technology;
import com.devpulse.backend.domain.port.out.TechnologyRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TechnologyPersistenceAdapter implements TechnologyRepositoryPort {

    private final TechnologyJpaRepository technologyJpaRepository;
    private final TechnologyMapper technologyMapper;
    public TechnologyPersistenceAdapter(TechnologyJpaRepository technologyJpaRepository, TechnologyMapper technologyMapper) {
        this.technologyJpaRepository = technologyJpaRepository;
        this.technologyMapper = technologyMapper;
    }

    @Override
    public Technology save(Technology technology) {
        TechnologyEntity technologyEntity = technologyMapper.toEntity(technology);
        return technologyMapper.toDomain(technologyJpaRepository.save(technologyEntity));
    }

    @Override
    public Optional<Technology> findById(Long id) {
        return technologyJpaRepository.findById(id).map(technologyMapper::toDomain);
    }

    @Override
    public List<Technology> findAll() {
        return technologyJpaRepository.findAll().stream().map(technologyMapper::toDomain).toList();
    }

    @Override
    public Page<Technology> findAll(Pageable pageable) {
        return technologyJpaRepository.findAll(pageable).map(technologyMapper::toDomain);
    }

    @Override
    public List<Technology> findByType(String type) {
        return technologyJpaRepository.findByType(type).stream().map(technologyMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        technologyJpaRepository.deleteById(id);
    }
}
