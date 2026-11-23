package br.edu.fatecpg.eduspring.service;

import br.edu.fatecpg.eduspring.dto.ProfessorRequest;
import br.edu.fatecpg.eduspring.dto.ProfessorResponse;
import br.edu.fatecpg.eduspring.exception.NegocioException;
import br.edu.fatecpg.eduspring.model.Disciplina;
import br.edu.fatecpg.eduspring.model.Professor;
import br.edu.fatecpg.eduspring.model.Turma;
import br.edu.fatecpg.eduspring.repository.DisciplinaRepository;
import br.edu.fatecpg.eduspring.repository.ProfessorRepository;
import br.edu.fatecpg.eduspring.repository.TurmaRepository;
import br.edu.fatecpg.eduspring.util.TextoUtils;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private static final int LIMITE_TURMAS = 2;
    private static final int LIMITE_DISCIPLINA = 3;

    private final ProfessorRepository professores;
    private final DisciplinaRepository disciplinas;
    private final TurmaRepository turmas;

    @Transactional(readOnly = true)
    public List<ProfessorResponse> listar() {
        return professores.listarComTurmas().stream().map(ProfessorResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ProfessorResponse buscarPorId(Long id) {
        return ProfessorResponse.de(buscar(id));
    }

    @Transactional
    public ProfessorResponse criar(ProfessorRequest r) {
        validarNomeDuplicado(r.nome(), null);
        Disciplina disciplina = buscarDisciplina(r.disciplinaId());
        List<Turma> turmasSelecionadas = validarRelacoes(null, disciplina, r.turmaIds());

        Professor professor = new Professor();
        professor.setNome(TextoUtils.limpar(r.nome()));
        professor.setDisciplina(disciplina);
        professor.setTurmas(new LinkedHashSet<>(turmasSelecionadas));
        return ProfessorResponse.de(professores.save(professor));
    }

    @Transactional
    public ProfessorResponse atualizar(Long id, ProfessorRequest r) {
        Professor professor = buscar(id);
        validarNomeDuplicado(r.nome(), id);
        Disciplina disciplina = buscarDisciplina(r.disciplinaId());
        List<Turma> turmasSelecionadas = validarRelacoes(id, disciplina, r.turmaIds());

        professor.setNome(TextoUtils.limpar(r.nome()));
        professor.setDisciplina(disciplina);
        professor.setTurmas(new LinkedHashSet<>(turmasSelecionadas));
        return ProfessorResponse.de(professor);
    }

    @Transactional
    public void remover(Long id) {
        professores.delete(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<Disciplina> disciplinasDisponiveis(Long professorId) {
        Professor atual = professorId == null ? null : buscar(professorId);
        return disciplinas.findAll().stream()
                .filter(d -> professores.countByDisciplinaId(d.getId()) < LIMITE_DISCIPLINA
                        || atual != null && atual.getDisciplina().getId().equals(d.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Turma> turmasDisponiveis(Long professorId) {
        Professor atual = professorId == null ? null : buscar(professorId);
        return turmas.findAll().stream()
                .filter(t -> professores.contarResponsaveisPorTurma(t.getId()) == 0
                        || atual != null && atual.getTurmas().stream()
                                .anyMatch(atualTurma -> atualTurma.getId().equals(t.getId())))
                .toList();
    }

    private List<Turma> validarRelacoes(Long professorId, Disciplina disciplina, List<Long> turmaIds) {
        List<Long> ids = turmaIds == null ? List.of() : turmaIds.stream().distinct().toList();
        if (ids.size() > LIMITE_TURMAS)
            throw NegocioException.invalido("Um professor pode ser responsável por no máximo 2 turmas.");

        long quantidadeDisciplina = professores.countByDisciplinaId(disciplina.getId());
        Professor atual = professorId == null ? null : buscar(professorId);
        boolean disciplinaAtual = atual != null
                && atual.getDisciplina().getId().equals(disciplina.getId());
        if (!disciplinaAtual && quantidadeDisciplina >= LIMITE_DISCIPLINA)
            throw NegocioException.conflito("Esta disciplina já possui 3 professores responsáveis.");

        List<Turma> selecionadas = ids.stream()
                .map(id -> turmas.findById(id)
                        .orElseThrow(() -> NegocioException.naoEncontrado("Turma não encontrada.")))
                .toList();

        for (Turma turma : selecionadas) {
            boolean turmaDoProfessor = atual != null && atual.getTurmas().stream()
                    .anyMatch(t -> t.getId().equals(turma.getId()));
            if (!turmaDoProfessor && professores.contarResponsaveisPorTurma(turma.getId()) > 0)
                throw NegocioException.conflito("A turma " + turma.getIdentificador()
                        + " já possui um professor responsável.");
        }
        return selecionadas;
    }

    private void validarNomeDuplicado(String nome, Long professorId) {
        String nomeLimpo = TextoUtils.limpar(nome);
        boolean duplicado = professores.findAll().stream()
                .anyMatch(p -> !p.getId().equals(professorId)
                        && TextoUtils.equivalentes(p.getNome(), nomeLimpo));
        if (duplicado)
            throw NegocioException.conflito("Já existe um professor cadastrado com este nome.");
    }

    private Disciplina buscarDisciplina(Long id) {
        return disciplinas.findById(id)
                .orElseThrow(() -> NegocioException.naoEncontrado("Disciplina não encontrada."));
    }

    private Professor buscar(Long id) {
        return professores.findById(id)
                .orElseThrow(() -> NegocioException.naoEncontrado("Professor não encontrado."));
    }
}
