package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TurmaForm {
    @NotBlank(message = "Informe o identificador")
    @Size(max = 50, message = "O identificador deve ter no máximo 50 caracteres")
    private String identificador;

    private Long professorId;
}
