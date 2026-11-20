package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;

public record TurmaRequest(@NotBlank String identificador) {}
