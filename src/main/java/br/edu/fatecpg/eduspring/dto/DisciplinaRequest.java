package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DisciplinaRequest(
        @NotBlank(message = "Informe o nome da disciplina")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome) {}
