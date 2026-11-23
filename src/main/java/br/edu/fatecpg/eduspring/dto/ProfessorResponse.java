package br.edu.fatecpg.eduspring.dto;

import br.edu.fatecpg.eduspring.model.Professor;
import java.util.List;

public record ProfessorResponse(Long id, String nome, Long disciplinaId, String disciplina,
                                List<Long> turmaIds, List<String> turmas) {
    public static ProfessorResponse de(Professor professor) {
        var turmasOrdenadas = professor.getTurmas().stream()
                .sorted((a, b) -> a.getIdentificador().compareToIgnoreCase(b.getIdentificador()))
                .toList();
        return new ProfessorResponse(
                professor.getId(),
                professor.getNome(),
                professor.getDisciplina().getId(),
                professor.getDisciplina().getNome(),
                turmasOrdenadas.stream().map(t -> t.getId()).toList(),
                turmasOrdenadas.stream().map(t -> t.getIdentificador()).toList());
    }
}
