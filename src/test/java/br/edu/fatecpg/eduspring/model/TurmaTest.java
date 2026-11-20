package br.edu.fatecpg.eduspring.model;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.fatecpg.eduspring.exception.NegocioException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TurmaTest {

    private Aluno aluno(String matricula, String nota) {
        Aluno a = new Aluno();
        a.setMatricula(matricula);
        a.setNome("Aluno " + matricula);
        a.setIdade(20);
        if (nota != null) {
            Disciplina d = new Disciplina();
            d.setId(1L);
            d.setNome("A");
            a.adicionarDisciplina(d).atribuirNota(new BigDecimal(nota));
        }
        return a;
    }

    @Test
    void mediaDaTurmaEhMediaDasMediasDosAlunos() {
        Turma t = new Turma();
        t.adicionarAluno(aluno("1", "8"));
        t.adicionarAluno(aluno("2", "6"));
        t.adicionarAluno(aluno("3", null));
        assertEquals(new BigDecimal("7.00"), t.calcularMediaTurma());
    }

    @Test
    void turmaVaziaTemMediaZero() {
        assertEquals(BigDecimal.ZERO, new Turma().calcularMediaTurma());
    }

    @Test
    void naoPermiteAlunoDuplicado() {
        Turma t = new Turma();
        t.adicionarAluno(aluno("1", null));
        assertThrows(NegocioException.class, () -> t.adicionarAluno(aluno("1", null)));
    }

    @Test
    void adicionarEBuscarEremoverAluno() {
        Turma t = new Turma();
        Aluno a = aluno("1", null);
        t.adicionarAluno(a);
        assertTrue(t.buscarAluno("1").isPresent());
        assertSame(t, a.getTurma());
        t.removerAluno(a);
        assertTrue(t.buscarAluno("1").isEmpty());
        assertNull(a.getTurma());
    }
}
