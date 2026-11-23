package br.edu.fatecpg.eduspring.service;

import br.edu.fatecpg.eduspring.dto.AlunoRequest;
import br.edu.fatecpg.eduspring.dto.AlunoResponse;
import br.edu.fatecpg.eduspring.dto.BoletimItem;
import br.edu.fatecpg.eduspring.dto.BoletimResponse;
import br.edu.fatecpg.eduspring.exception.NegocioException;
import br.edu.fatecpg.eduspring.model.Aluno;
import br.edu.fatecpg.eduspring.model.Disciplina;
import br.edu.fatecpg.eduspring.model.Nota;
import br.edu.fatecpg.eduspring.model.Turma;
import br.edu.fatecpg.eduspring.repository.AlunoRepository;
import br.edu.fatecpg.eduspring.repository.DisciplinaRepository;
import br.edu.fatecpg.eduspring.repository.TurmaRepository;
import br.edu.fatecpg.eduspring.util.TextoUtils;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository alunos;
    private final TurmaRepository turmas;
    private final DisciplinaRepository disciplinas;

    @Transactional(readOnly = true)
    public List<AlunoResponse> listar(String nome, String turma, String ordem) {
        List<Aluno> lista;
        if (nome != null && !nome.isBlank()) {
            String nomeNormalizado = TextoUtils.normalizar(nome);
            lista = alunos.findAll().stream()
                    .filter(a -> TextoUtils.normalizar(a.getNome()).contains(nomeNormalizado))
                    .toList();
        }
        else if (turma != null && !turma.isBlank()) {
            lista = alunos.findAll().stream()
                    .filter(a -> a.getTurma() != null
                            && TextoUtils.equivalentes(a.getTurma().getIdentificador(), turma))
                    .toList();
        }
        else if ("idade".equals(ordem)) lista = alunos.listarOrdenadosPorIdade();
        else if ("media".equals(ordem)) lista = alunos.listarOrdenadosPorMedia();
        else lista = alunos.findAll();
        return lista.stream().map(AlunoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscarPorId(Long id) {
        return AlunoResponse.de(buscar(id));
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscarPorMatricula(String matricula) {
        return AlunoResponse.de(alunos.findAll().stream()
                .filter(a -> TextoUtils.equivalentes(a.getMatricula(), matricula))
                .findFirst()
                .orElseThrow(() -> NegocioException.naoEncontrado("Nenhum aluno foi encontrado com esta matrícula.")));
    }

    @Transactional
    public AlunoResponse criar(AlunoRequest r) {
        validarIdade(r.idade());
        String matricula = TextoUtils.limpar(r.matricula());
        if (alunos.findAll().stream()
                .anyMatch(a -> TextoUtils.equivalentes(a.getMatricula(), matricula)))
            throw NegocioException.conflito("Esta matrícula já está cadastrada.");
        Aluno a = new Aluno();
        a.setMatricula(matricula);
        a.setNome(TextoUtils.limpar(r.nome()));
        a.setIdade(r.idade());
        definirTurma(a, r.turmaId());
        return AlunoResponse.de(alunos.save(a));
    }

    @Transactional
    public AlunoResponse atualizar(Long id, AlunoRequest r) {
        validarIdade(r.idade());
        Aluno a = buscar(id);
        String matricula = TextoUtils.limpar(r.matricula());
        boolean duplicada = alunos.findAll().stream()
                .anyMatch(outro -> !outro.getId().equals(id)
                        && TextoUtils.equivalentes(outro.getMatricula(), matricula));
        if (duplicada) throw NegocioException.conflito("Já existe outro aluno com esta matrícula.");
        a.setMatricula(matricula);
        a.setNome(TextoUtils.limpar(r.nome()));
        a.setIdade(r.idade());
        definirTurma(a, r.turmaId());
        return AlunoResponse.de(a);
    }

    @Transactional
    public void remover(Long id) {
        Aluno a = buscar(id);
        if (a.getTurma() != null) a.getTurma().removerAluno(a);
        alunos.delete(a);
    }

    @Transactional(readOnly = true)
    public BoletimResponse boletim(Long id) {
        Aluno a = buscar(id);
        var itens = a.getNotas().stream()
                .map(n -> new BoletimItem(n.getDisciplina().getId(), n.getDisciplina().getNome(), n.getValor()))
                .toList();
        return new BoletimResponse(a.getId(), a.getNome(), a.calcularMedia(), itens);
    }

    @Transactional
    public BoletimItem lancarNota(Long alunoId, Long disciplinaId, BigDecimal valor) {
        Aluno a = buscar(alunoId);
        Disciplina d = disciplinas.findById(disciplinaId)
                .orElseThrow(() -> NegocioException.naoEncontrado("Disciplina não encontrada."));
        Nota nota = a.getNotas().stream()
                .filter(n -> n.getDisciplina().getId().equals(disciplinaId))
                .findFirst()
                .orElseGet(() -> a.adicionarDisciplina(d));
        nota.atribuirNota(valor);
        alunos.save(a);
        return new BoletimItem(d.getId(), d.getNome(), nota.getValor());
    }

    @Transactional
    public void removerNota(Long alunoId, Long disciplinaId) {
        Aluno aluno = buscar(alunoId);
        Disciplina disciplina = disciplinas.findById(disciplinaId)
                .orElseThrow(() -> NegocioException.naoEncontrado("Disciplina não encontrada."));
        aluno.removerDisciplina(disciplina);
    }

    private Aluno buscar(Long id) {
        return alunos.findById(id).orElseThrow(() -> NegocioException.naoEncontrado("Aluno não encontrado."));
    }

    private void validarIdade(Integer idade) {
        if (idade == null || idade < 12)
            throw NegocioException.invalido("A idade mínima para cadastrar um aluno é 12 anos.");
        if (idade > 120)
            throw NegocioException.invalido("A idade máxima permitida é 120 anos.");
    }

    private void definirTurma(Aluno a, Long turmaId) {
        if (a.getTurma() != null) a.getTurma().removerAluno(a);
        if (turmaId == null) return;
        Turma t = turmas.findById(turmaId)
                .orElseThrow(() -> NegocioException.naoEncontrado("Turma não encontrada."));
        t.adicionarAluno(a);
    }
}
