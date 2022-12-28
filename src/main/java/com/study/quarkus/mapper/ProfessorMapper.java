package com.study.quarkus.mapper;

import com.study.quarkus.model.Professor;
import com.study.quarkus.dto.ProfessorResponse;
import com.study.quarkus.dto.ProfessorRequest;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class ProfessorMapper {
    public List<ProfessorResponse> toResponse(List<Professor> listOfEntities) {

        if (Objects.isNull(listOfEntities))
            return new ArrayList<>();

        return listOfEntities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProfessorResponse toResponse(Professor entity) {

        if (Objects.isNull(entity))
            return null;

        var formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY hh:mm:ss");

        return ProfessorResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .dateTime(formatter.format(entity.getDateTime()))
                .build();
    }

    public Professor toEntity(ProfessorRequest request) {
        if (Objects.isNull(request)) {
            return null;
        } else {
            return Professor.builder()
                    .name(request.getName())
                    .build();
        }
    }
}
