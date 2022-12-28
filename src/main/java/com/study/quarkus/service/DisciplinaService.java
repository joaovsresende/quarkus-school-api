package com.study.quarkus.service;

import com.study.quarkus.dto.DisciplinaRequest;
import com.study.quarkus.dto.DisciplinaResponse;
import com.study.quarkus.dto.TitularResponse;
import com.study.quarkus.exception.InvalidStateException;
import com.study.quarkus.mapper.DisciplinaMapper;
import com.study.quarkus.model.Disciplina;
import com.study.quarkus.repository.DisciplinaRepository;
import com.study.quarkus.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class DisciplinaService {

    private final DisciplinaMapper mapper;
    private final DisciplinaRepository repository;
    private final ProfessorRepository professorRepository;

    public List<DisciplinaResponse> retrieveAll() {
        log.info("Listing disciplinas");
        final var listOfEntities = repository.listAll();
        return mapper.toResponse(listOfEntities);
    }

    @Transactional
    public DisciplinaResponse save(@Valid DisciplinaRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        log.info("Saving disciplina - {}", request);

        var entity = Disciplina.builder()
                .name(request.getName())
                .build();

        repository.persist(entity);

        return mapper.toResponse(entity);
    }

    public DisciplinaResponse getById(int id) {
        log.info("Getting professor id-{}", id);

        var entity = repository.findById(id);
        return mapper.toResponse(entity);
    }

    @Transactional
    public TitularResponse updateTitular(int idDisciplina, int idProfessor) {

        log.info("Updating titular disciplina-id: {}, professor-id: {}", idDisciplina, idProfessor);

        var disciplina = repository.findById(idDisciplina);
        var professor = professorRepository.findById(idProfessor);

        if (Objects.isNull(disciplina))
            throw new EntityNotFoundException("Disciplina not found");
        if (Objects.isNull(professor))
            throw new EntityNotFoundException("Professor not found");

        var query = repository.find("titular", professor);
        if (query.count() > 0)
            throw new InvalidStateException("Professor must have at most one Disciplina as titular");

        disciplina.setTitular(professor);
        repository.persist(disciplina);

        return mapper.toResponse(professor);
    }

    public DisciplinaResponse getDisciplinaByProfessorId(int idProfessor) {

        log.info("Getting disciplina by professor-id: {}", idProfessor);

        var professor = professorRepository.findById(idProfessor);
        if (Objects.isNull(professor))
            throw new EntityNotFoundException("Professor not found");

        var query = repository.find("titular", professor);
        if (query.count() == 0)
            throw new EntityNotFoundException("Disciplina not found");
        if (query.count() > 1)
            throw new InvalidStateException("Professor must have at most one Disciplina as titular");

        var disciplina = query.singleResult();

        return mapper.toResponse(disciplina);
    }
}