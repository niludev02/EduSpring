package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DisciplinaForm {
    @NotBlank(message = "Informe o nome")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;
}
