package br.edu.fatecpg.eduspring.service;

import br.edu.fatecpg.eduspring.dto.DisciplinaRequest;
import br.edu.fatecpg.eduspring.dto.DisciplinaResponse;
import br.edu.fatecpg.eduspring.exception.NegocioException;
import br.edu.fatecpg.eduspring.model.Disciplina;
import br.edu.fatecpg.eduspring.repository.DisciplinaRepository;
import br.edu.fatecpg.eduspring.repository.ProfessorRepository;
import br.edu.fatecpg.eduspring.util.TextoUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DisciplinaService {
    private final DisciplinaRepository disciplinas;
    private final ProfessorRepository professores;

    @Transactional(readOnly = true)
    public List<DisciplinaResponse> listar() {
        return disciplinas.findAll().stream()
                .map(d -> DisciplinaResponse.de(d, disciplinas.possuiNotas(d.getId()),
                        professores.countByDisciplinaId(d.getId()) > 0))
                .toList();
    }

    @Transactional
    public DisciplinaResponse criar(DisciplinaRequest r) {
        String nome = TextoUtils.limpar(r.nome());
        if (disciplinas.findAll().stream()
                .anyMatch(d -> TextoUtils.equivalentes(d.getNome(), nome)))
            throw NegocioException.conflito("Esta disciplina já está cadastrada.");
        Disciplina d = new Disciplina();
        d.setNome(nome);
        return DisciplinaResponse.de(disciplinas.save(d), false, false);
    }

    @Transactional(readOnly = true)
    public DisciplinaResponse buscarPorId(Long id) {
        Disciplina disciplina = buscar(id);
        return DisciplinaResponse.de(disciplina, disciplinas.possuiNotas(id),
                professores.countByDisciplinaId(id) > 0);
    }

    @Transactional
    public DisciplinaResponse atualizar(Long id, DisciplinaRequest r) {
        Disciplina disciplina = buscar(id);
        String nome = TextoUtils.limpar(r.nome());
        boolean duplicada = disciplinas.findAll().stream()
                .anyMatch(d -> !d.getId().equals(id)
                        && TextoUtils.equivalentes(d.getNome(), nome));
        if (duplicada) throw NegocioException.conflito("Já existe outra disciplina com este nome.");
        disciplina.setNome(nome);
        return DisciplinaResponse.de(disciplina, disciplinas.possuiNotas(id),
                professores.countByDisciplinaId(id) > 0);
    }

    @Transactional
    public void remover(Long id) {
        Disciplina disciplina = buscar(id);
        if (disciplinas.possuiNotas(id))
            throw NegocioException.conflito("Não é possível excluir esta disciplina: ela possui notas vinculadas.");
        if (professores.countByDisciplinaId(id) > 0)
            throw NegocioException.conflito("Não é possível excluir esta disciplina: ela possui professores vinculados.");
        disciplinas.delete(disciplina);
    }

    private Disciplina buscar(Long id) {
        return disciplinas.findById(id)
                .orElseThrow(() -> NegocioException.naoEncontrado("Disciplina não encontrada."));
    }
}
