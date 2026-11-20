package br.edu.fatecpg.eduspring.dto;

import br.edu.fatecpg.eduspring.model.Turma;
import java.math.BigDecimal;

public record TurmaResponse(Long id, String identificador, int quantidadeAlunos, BigDecimal media) {
    public static TurmaResponse de(Turma t) {
        return new TurmaResponse(t.getId(), t.getIdentificador(), t.getAlunos().size(), t.calcularMediaTurma());
    }
}
