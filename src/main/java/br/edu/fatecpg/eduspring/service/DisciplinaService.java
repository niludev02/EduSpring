package br.edu.fatecpg.eduspring.service;

import br.edu.fatecpg.eduspring.dto.DisciplinaRequest;
import br.edu.fatecpg.eduspring.dto.DisciplinaResponse;
import br.edu.fatecpg.eduspring.exception.NegocioException;
import br.edu.fatecpg.eduspring.model.Disciplina;
import br.edu.fatecpg.eduspring.repository.DisciplinaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisciplinaService {
    private final DisciplinaRepository disciplinas;

    public List<DisciplinaResponse> listar() {
        return disciplinas.findAll().stream().map(DisciplinaResponse::de).toList();
    }

    public DisciplinaResponse criar(DisciplinaRequest r) {
        if (disciplinas.findByNome(r.nome()).isPresent())
            throw NegocioException.conflito("Disciplina já cadastrada");
        Disciplina d = new Disciplina();
        d.setNome(r.nome());
        return DisciplinaResponse.de(disciplinas.save(d));
    }
}
