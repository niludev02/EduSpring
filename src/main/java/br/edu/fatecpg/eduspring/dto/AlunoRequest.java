package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AlunoRequest(
        @NotBlank String matricula,
        @NotBlank String nome,
        @NotNull @Positive Integer idade,
        Long turmaId) {}
