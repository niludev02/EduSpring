package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfessorForm {
    @NotBlank(message = "Informe o nome do professor")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotNull(message = "Selecione uma disciplina")
    private Long disciplinaId;

    private List<Long> turmaIds = new ArrayList<>();
}
