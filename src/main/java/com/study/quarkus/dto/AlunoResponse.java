package com.study.quarkus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class AlunoResponse {

    private int id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tutor;

    private String dateTime;
    
}