package com.study.quarkus.repository;

import com.study.quarkus.model.Aluno;
import com.study.quarkus.model.Professor;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;

import java.util.List;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlunoRepository implements PanacheRepositoryBase<Aluno, Integer> {
    public List<Aluno> getTutoradosByProfessor(Professor professor) {

        Objects.requireNonNull(professor, "Professor não pode ser null ou vazio");

        var query = find("tutor", Sort.ascending("name"), professor);

        return query.list();
    }
}