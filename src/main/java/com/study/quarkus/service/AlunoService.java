package com.study.quarkus.service;

import com.study.quarkus.dto.AlunoRequest;
import com.study.quarkus.dto.AlunoResponse;
import com.study.quarkus.mapper.AlunoMapper;
import com.study.quarkus.model.Aluno;
import com.study.quarkus.dto.TutorResponse;
import com.study.quarkus.repository.AlunoRepository;
import com.study.quarkus.repository.ProfessorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoMapper mapper;
    private final AlunoRepository repository;
    private final ProfessorRepository professorRepository;

    public List<AlunoResponse> retrieveAll() {
        log.info("Listing alunos");
        final var listOfEntities = repository.listAll();
        return mapper.toResponse(listOfEntities);
    }

    @Transactional
    public AlunoResponse save(@Valid AlunoRequest request) {
        Objects.requireNonNull(request, "request não pode ser null");

        log.info("Salvando aluno - {}", request);

        var entity = Aluno.builder()
                .name(request.getName())
                .build();

        repository.persist(entity);

        return mapper.toResponse(entity);
    }

    public AlunoResponse getById(int id) {
        log.info("Getting aluno id-{}", id);

        var entity = repository.findById(id);
        if (Objects.isNull(entity))
            throw new EntityNotFoundException("Aluno não encontrado");

        return mapper.toResponse(entity);
    }

    @Transactional
    public TutorResponse updateTutor(int idAluno, int idProfessor) {
        log.info("Atualizando tutor -> id aluno: {}, id professor {} ", idAluno, idProfessor);

        var aluno = repository.findById(idAluno);
        var professor = professorRepository.findById(idProfessor);

        if (Objects.isNull(aluno))
            throw new EntityNotFoundException("Aluno não encontrado");
        if (Objects.isNull(professor))
            throw new EntityNotFoundException("Professor não encontado");

        aluno.setTutor(professor);
        repository.persist(aluno);

        return mapper.toResponse(professor);
    }

    public List<AlunoResponse> getTutoradosByProfessorId(int idProfessor) {

        log.info("Obtendo tutorados por id de professor: {}", idProfessor);

        var professor = professorRepository.findById(idProfessor);

        if (Objects.isNull(professor))
            throw new EntityNotFoundException("Professor não encontrado");

        List<Aluno> listOfEntities = repository.getTutoradosByProfessor(professor);

        return mapper.toResponse(listOfEntities);
    }
}