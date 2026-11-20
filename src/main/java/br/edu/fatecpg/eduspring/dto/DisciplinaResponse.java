package br.edu.fatecpg.eduspring.dto;

import br.edu.fatecpg.eduspring.model.Disciplina;

public record DisciplinaResponse(Long id, String nome) {
    public static DisciplinaResponse de(Disciplina d) {
        return new DisciplinaResponse(d.getId(), d.getNome());
    }
}
