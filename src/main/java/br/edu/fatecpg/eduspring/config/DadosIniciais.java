package br.edu.fatecpg.eduspring.config;

import br.edu.fatecpg.eduspring.dto.AlunoRequest;
import br.edu.fatecpg.eduspring.dto.DisciplinaRequest;
import br.edu.fatecpg.eduspring.dto.DisciplinaResponse;
import br.edu.fatecpg.eduspring.dto.TurmaRequest;
import br.edu.fatecpg.eduspring.repository.TurmaRepository;
import br.edu.fatecpg.eduspring.service.AlunoService;
import br.edu.fatecpg.eduspring.service.DisciplinaService;
import br.edu.fatecpg.eduspring.service.TurmaService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DadosIniciais implements CommandLineRunner {
    private final TurmaRepository turmaRepo;
    private final TurmaService turmas;
    private final DisciplinaService disciplinas;
    private final AlunoService alunos;

    @Override
    public void run(String... args) {
        if (turmaRepo.count() > 0) return;

        var t1 = turmas.criar(new TurmaRequest("DSM-1"));
        var t2 = turmas.criar(new TurmaRequest("DSM-2"));

        List<DisciplinaResponse> ds = List.of(
                disciplinas.criar(new DisciplinaRequest("Algoritmos")),
                disciplinas.criar(new DisciplinaRequest("Banco de Dados")),
                disciplinas.criar(new DisciplinaRequest("Desenvolvimento Web")));

        aluno("2026001", "Ana Souza", 19, t1.id(), ds, 9.0, 8.5, 9.5);
        aluno("2026002", "Bruno Lima", 21, t1.id(), ds, 6.0, 7.0, 5.5);
        aluno("2026003", "Carla Mendes", 20, t1.id(), ds, 7.5, 8.0);
        aluno("2026004", "Diego Santos", 22, t2.id(), ds, 4.0, 5.0, 6.0);
        aluno("2026005", "Elisa Rocha", 18, t2.id(), ds, 10.0, 9.0, 9.5);
        aluno("2026006", "Felipe Costa", 23, null, ds);
    }

    private void aluno(String matricula, String nome, int idade, Long turmaId,
                       List<DisciplinaResponse> ds, double... notas) {
        var a = alunos.criar(new AlunoRequest(matricula, nome, idade, turmaId));
        for (int i = 0; i < notas.length; i++)
            alunos.lancarNota(a.id(), ds.get(i).id(), BigDecimal.valueOf(notas[i]));
    }
}
