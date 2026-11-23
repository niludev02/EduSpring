package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TurmaRequest(
        @NotBlank(message = "Informe o identificador da turma")
        @Size(max = 50, message = "O identificador deve ter no máximo 50 caracteres")
        String identificador,
        Long professorId) {
    public TurmaRequest(String identificador) {
        this(identificador, null);
    }
}
