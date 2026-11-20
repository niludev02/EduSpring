package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DisciplinaForm {
    @NotBlank(message = "Informe o nome")
    private String nome;
}
