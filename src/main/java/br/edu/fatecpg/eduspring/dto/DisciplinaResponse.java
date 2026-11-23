package br.edu.fatecpg.eduspring.dto;

import br.edu.fatecpg.eduspring.model.Disciplina;

public record DisciplinaResponse(Long id, String nome, boolean possuiNotas, boolean possuiProfessores) {
    public static DisciplinaResponse de(Disciplina d) {
        return new DisciplinaResponse(d.getId(), d.getNome(), false, false);
    }

    public static DisciplinaResponse de(Disciplina d, boolean possuiNotas) {
        return new DisciplinaResponse(d.getId(), d.getNome(), possuiNotas, false);
    }

    public static DisciplinaResponse de(Disciplina d, boolean possuiNotas, boolean possuiProfessores) {
        return new DisciplinaResponse(d.getId(), d.getNome(), possuiNotas, possuiProfessores);
    }
}
