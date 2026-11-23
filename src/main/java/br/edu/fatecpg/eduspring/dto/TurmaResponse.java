package br.edu.fatecpg.eduspring.dto;

import br.edu.fatecpg.eduspring.model.Turma;
import java.math.BigDecimal;

public record TurmaResponse(Long id, String identificador, int quantidadeAlunos, BigDecimal media,
                            boolean possuiProfessor, Long professorId, String professor) {
    public static TurmaResponse de(Turma t) {
        return de(t, false);
    }

    public static TurmaResponse de(Turma t, boolean possuiProfessor) {
        return new TurmaResponse(t.getId(), t.getIdentificador(), t.getAlunos().size(),
                t.calcularMediaTurma(), possuiProfessor, null, null);
    }

    public static TurmaResponse de(Turma t, boolean possuiProfessor, Long professorId, String professor) {
        return new TurmaResponse(t.getId(), t.getIdentificador(), t.getAlunos().size(),
                t.calcularMediaTurma(), possuiProfessor, professorId, professor);
    }
}
