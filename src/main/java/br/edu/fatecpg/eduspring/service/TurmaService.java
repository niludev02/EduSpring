package br.edu.fatecpg.eduspring.service;

import br.edu.fatecpg.eduspring.dto.TurmaRequest;
import br.edu.fatecpg.eduspring.dto.TurmaResponse;
import br.edu.fatecpg.eduspring.exception.NegocioException;
import br.edu.fatecpg.eduspring.model.Turma;
import br.edu.fatecpg.eduspring.repository.AlunoRepository;
import br.edu.fatecpg.eduspring.repository.TurmaRepository;
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

    @Transactional(readOnly = true)
    public List<TurmaResponse> listar() {
        return turmas.findAll().stream().map(TurmaResponse::de).toList();
    }

    @Transactional
    public TurmaResponse criar(TurmaRequest r) {
        if (turmas.findByIdentificador(r.identificador()).isPresent())
            throw NegocioException.conflito("Turma já cadastrada");
        Turma t = new Turma();
        t.setIdentificador(r.identificador());
        return TurmaResponse.de(turmas.save(t));
    }

    @Transactional(readOnly = true)
    public BigDecimal media(Long id) {
        return turmas.findById(id)
                .orElseThrow(() -> NegocioException.naoEncontrado("Turma não encontrada"))
                .calcularMediaTurma();
    }

    public long quantidadeAlunos(String identificador) {
        return alunos.contarAlunosPorTurma(identificador);
    }
}
