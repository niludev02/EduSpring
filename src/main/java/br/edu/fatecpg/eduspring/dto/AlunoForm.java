package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlunoForm {
    @NotBlank(message = "Informe a matrícula")
    private String matricula;

    @NotBlank(message = "Informe o nome")
    private String nome;

    @NotNull(message = "Informe a idade")
    @Positive(message = "A idade deve ser positiva")
    private Integer idade;

    private Long turmaId;
}
