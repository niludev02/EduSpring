package br.edu.fatecpg.eduspring.model;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.fatecpg.eduspring.exception.NegocioException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AlunoTest {

    private Disciplina disciplina(Long id, String nome) {
        Disciplina d = new Disciplina();
        d.setId(id);
        d.setNome(nome);
        return d;
    }

    private Aluno aluno(String matricula) {
        Aluno a = new Aluno();
        a.setMatricula(matricula);
        a.setNome("Teste");
        a.setIdade(20);
        return a;
    }

    @Test
    void mediaSemNotasEhZero() {
        assertEquals(BigDecimal.ZERO, aluno("1").calcularMedia());
    }

    @Test
    void calculaMediaDasNotas() {
        Aluno a = aluno("1");
        a.adicionarDisciplina(disciplina(1L, "A")).atribuirNota(new BigDecimal("8"));
        a.adicionarDisciplina(disciplina(2L, "B")).atribuirNota(new BigDecimal("7"));
        assertEquals(new BigDecimal("7.50"), a.calcularMedia());
    }

    @Test
    void ignoraDisciplinaSemNotaNaMedia() {
        Aluno a = aluno("1");
        a.adicionarDisciplina(disciplina(1L, "A")).atribuirNota(new BigDecimal("9"));
        a.adicionarDisciplina(disciplina(2L, "B"));
        assertEquals(new BigDecimal("9.00"), a.calcularMedia());
    }

    @Test
    void naoPermiteDisciplinaDuplicada() {
        Aluno a = aluno("1");
        Disciplina d = disciplina(1L, "A");
        a.adicionarDisciplina(d);
        assertThrows(NegocioException.class, () -> a.adicionarDisciplina(d));
    }

    @Test
    void removerDisciplinaInexistenteLancaExcecao() {
        assertThrows(NegocioException.class, () -> aluno("1").removerDisciplina(disciplina(1L, "A")));
    }
}
