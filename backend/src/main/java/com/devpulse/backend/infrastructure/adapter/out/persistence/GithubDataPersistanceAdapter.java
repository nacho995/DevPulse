package com.devpulse.backend.infrastructure.adapter.out.persistence;

import com.devpulse.backend.domain.model.GithubData;
import com.devpulse.backend.domain.port.out.GithubDataRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GithubDataPersistanceAdapter implements GithubDataRepositoryPort {

    private final GithubDataMapper mapper;
    private final GithubDataJpaRepository jpaRepository;
    public GithubDataPersistanceAdapter(GithubDataMapper mapper, GithubDataJpaRepository jpaRepository) {
        this.mapper = mapper;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<GithubData> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public GithubData save(GithubData githubData) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(githubData)));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<GithubData> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<GithubData> findByTechnologyId(Long id) {
        return jpaRepository.findByTechnologyId(id).stream().map(mapper::toDomain).toList();
    }
}
