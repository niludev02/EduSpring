package br.edu.fatecpg.eduspring.service;

import br.edu.fatecpg.eduspring.dto.TurmaRequest;
import br.edu.fatecpg.eduspring.dto.TurmaResponse;
import br.edu.fatecpg.eduspring.exception.NegocioException;
import br.edu.fatecpg.eduspring.model.Turma;
import br.edu.fatecpg.eduspring.model.Professor;
import br.edu.fatecpg.eduspring.repository.AlunoRepository;
import br.edu.fatecpg.eduspring.repository.ProfessorRepository;
import br.edu.fatecpg.eduspring.repository.TurmaRepository;
import br.edu.fatecpg.eduspring.util.TextoUtils;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TurmaService {
    private final TurmaRepository turmas;
    private final AlunoRepository alunos;
    private final ProfessorRepository professores;

    @Transactional(readOnly = true)
    public List<TurmaResponse> listar() {
        return turmas.findAll().stream()
                .map(t -> {
                    var responsavel = professores.findByTurmasId(t.getId()).stream().findFirst();
                    return TurmaResponse.de(t, responsavel.isPresent(),
                            responsavel.map(Professor::getId).orElse(null),
                            responsavel.map(Professor::getNome).orElse(null));
                })
                .toList();
    }

    @Transactional
    public TurmaResponse criar(TurmaRequest r) {
        String identificador = TextoUtils.limpar(r.identificador());
        if (turmas.findAll().stream()
                .anyMatch(t -> TextoUtils.equivalentes(t.getIdentificador(), identificador)))
            throw NegocioException.conflito("Esta turma já está cadastrada.");
        Turma t = new Turma();
        t.setIdentificador(identificador);
        Turma salva = turmas.save(t);
        atribuirProfessor(salva, r.professorId());
        return TurmaResponse.de(salva, r.professorId() != null);
    }

    @Transactional(readOnly = true)
    public TurmaResponse buscarPorId(Long id) {
        Turma turma = buscar(id);
        var responsavel = professores.findByTurmasId(id).stream().findFirst();
        return TurmaResponse.de(turma, responsavel.isPresent(),
                responsavel.map(Professor::getId).orElse(null),
                responsavel.map(Professor::getNome).orElse(null));
    }

    @Transactional
    public TurmaResponse atualizar(Long id, TurmaRequest r) {
        Turma turma = buscar(id);
        String identificador = TextoUtils.limpar(r.identificador());
        boolean duplicada = turmas.findAll().stream()
                .anyMatch(t -> !t.getId().equals(id)
                        && TextoUtils.equivalentes(t.getIdentificador(), identificador));
        if (duplicada) throw NegocioException.conflito("Já existe outra turma com este identificador.");
        turma.setIdentificador(identificador);
        atribuirProfessor(turma, r.professorId());
        return TurmaResponse.de(turma, professores.contarResponsaveisPorTurma(id) > 0);
    }

    @Transactional
    public void remover(Long id) {
        Turma turma = buscar(id);
        if (!turma.getAlunos().isEmpty())
            throw NegocioException.conflito("Não é possível excluir esta turma: ela possui alunos vinculados.");
        if (professores.contarResponsaveisPorTurma(id) > 0)
            throw NegocioException.conflito("Não é possível excluir esta turma: ela possui um professor responsável.");
        turmas.delete(turma);
    }

    @Transactional(readOnly = true)
    public BigDecimal media(Long id) {
        return turmas.findById(id)
                .orElseThrow(() -> NegocioException.naoEncontrado("Turma não encontrada."))
                .calcularMediaTurma();
    }

    public long quantidadeAlunos(String identificador) {
        return alunos.contarAlunosPorTurma(identificador);
    }

    @Transactional(readOnly = true)
    public List<Professor> professoresDisponiveis(Long turmaId) {
        var atual = professores.findByTurmasId(turmaId);
        return professores.findAll().stream()
                .filter(p -> atual.stream().anyMatch(responsavel -> responsavel.getId().equals(p.getId()))
                        || (professores.contarResponsaveisPorTurma(turmaId) == 0
                            && p.getTurmas().size() < 2))
                .toList();
    }

    private void atribuirProfessor(Turma turma, Long professorId) {
        for (Professor atual : professores.findByTurmasId(turma.getId())) {
            atual.getTurmas().remove(turma);
            professores.save(atual);
        }
        if (professorId == null) return;
        Professor professor = professores.findById(professorId)
                .orElseThrow(() -> NegocioException.naoEncontrado("Professor não encontrado."));
        if (professor.getTurmas().size() >= 2
                && professor.getTurmas().stream().noneMatch(t -> t.getId().equals(turma.getId())))
            throw NegocioException.conflito("O professor já é responsável por 2 turmas.");
        if (professores.contarResponsaveisPorTurma(turma.getId()) > 0)
            throw NegocioException.conflito("Esta turma já possui um professor responsável.");
        professor.getTurmas().add(turma);
        professores.save(professor);
    }

    private Turma buscar(Long id) {
        return turmas.findById(id)
                .orElseThrow(() -> NegocioException.naoEncontrado("Turma não encontrada."));
    }
}
