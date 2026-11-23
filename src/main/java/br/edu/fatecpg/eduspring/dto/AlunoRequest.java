package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record AlunoRequest(
        @NotBlank(message = "Informe a matrícula do aluno")
        @Size(max = 30, message = "A matrícula deve ter no máximo 30 caracteres")
        String matricula,
        @NotBlank(message = "Informe o nome do aluno")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,
        @NotNull(message = "Informe a idade do aluno")
        @Min(value = 12, message = "A idade mínima para cadastrar um aluno é 12 anos")
        @Max(value = 120, message = "A idade máxima permitida é 120 anos")
        Integer idade,
        Long turmaId) {}
