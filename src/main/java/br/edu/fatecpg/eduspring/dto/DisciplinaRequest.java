package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;

public record DisciplinaRequest(@NotBlank String nome) {}
