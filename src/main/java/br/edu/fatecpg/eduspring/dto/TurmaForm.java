package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TurmaForm {
    @NotBlank(message = "Informe o identificador")
    private String identificador;
}
