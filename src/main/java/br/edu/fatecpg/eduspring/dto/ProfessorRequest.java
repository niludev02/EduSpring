package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ProfessorRequest(
        @NotBlank(message = "Informe o nome do professor")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,
        @NotNull(message = "Selecione uma disciplina")
        Long disciplinaId,
        List<Long> turmaIds) {}
