package br.edu.fatecpg.eduspring.dto;

import br.edu.fatecpg.eduspring.model.Aluno;
import br.edu.fatecpg.eduspring.model.Turma;
import java.math.BigDecimal;

public record AlunoResponse(Long id, String matricula, String nome, Integer idade,
                            Long turmaId, String turma, BigDecimal media) {
    public static AlunoResponse de(Aluno a) {
        Turma t = a.getTurma();
        return new AlunoResponse(a.getId(), a.getMatricula(), a.getNome(), a.getIdade(),
                t == null ? null : t.getId(), t == null ? null : t.getIdentificador(), a.calcularMedia());
    }
}
