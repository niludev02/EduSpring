package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 12, message = "A idade mínima para cadastrar um aluno é 12 anos")
    @Max(value = 120, message = "A idade máxima permitida é 120 anos")
    private Integer idade;

    private Long turmaId;
}
