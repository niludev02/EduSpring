package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AlunoRequest(
        @NotBlank(message = "Informe a matrícula do aluno")
        @Size(max = 30, message = "A matrícula deve ter no máximo 30 caracteres")
        String matricula,
        @NotBlank(message = "Informe o nome do aluno")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,
        @NotNull(message = "Informe a idade do aluno")
        @Positive(message = "A idade deve ser um número positivo")
        Integer idade,
        Long turmaId) {}
