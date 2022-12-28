package com.study.quarkus.service;

import com.study.quarkus.dto.ProfessorRequest;
import com.study.quarkus.dto.ProfessorResponse;
import com.study.quarkus.mapper.ProfessorMapper;
import com.study.quarkus.model.Professor;
import com.study.quarkus.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorMapper mapper;
    private final ProfessorRepository repository;

    public List<ProfessorResponse> retrieveAll() {
        log.info("Listando professores");
        final List<Professor> listOfProfessors = repository.listAll();
        return mapper.toResponse(listOfProfessors);
    }

    public ProfessorResponse getById(int id) {
        log.info("Obtendo professor id: {}", id);

        Professor professor = repository.findById(id);
        return mapper.toResponse(professor);
    }

    @Transactional
    public ProfessorResponse save(@Valid ProfessorRequest professorRequest) {

        Objects.requireNonNull(professorRequest, "A requisição não pode ser nula");

        log.info("Salvando professor - {}", professorRequest);

        Professor entity = Professor.builder()
                .name(professorRequest.getName())
                .build();

        repository.persistAndFlush(entity);

        return mapper.toResponse(entity);
    }

    @Transactional
    public ProfessorResponse update(int id, @Valid ProfessorRequest professorRequest) {

        log.info("Atualizando professor id: {}, dados: {}", id, professorRequest);

        Optional<Professor> professor = repository.findByIdOptional(id);

        professor.orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

        var entity = professor.get();
        entity.setName(professorRequest.getName());

        return mapper.toResponse(entity);
    }

    @Transactional
    public void delete(int id) {
        log.info("Deletando professor id: {}", id);

        Optional<Professor> professor = repository.findByIdOptional(id);

        professor.ifPresent(repository::delete);
    }
}