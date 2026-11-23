package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlunoForm {
    @NotBlank(message = "Informe a matrícula")
    @Size(max = 30, message = "A matrícula deve ter no máximo 30 caracteres")
    private String matricula;

    @NotBlank(message = "Informe o nome")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotNull(message = "Informe a idade")
    @Positive(message = "A idade deve ser positiva")
    private Integer idade;

    private Long turmaId;
}
