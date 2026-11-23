package br.edu.fatecpg.eduspring.model;

import br.edu.fatecpg.eduspring.exception.NegocioException;
import br.edu.fatecpg.eduspring.util.TextoUtils;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Turma {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identificador;

    @OneToMany(mappedBy = "turma")
    private List<Aluno> alunos = new ArrayList<>();

    public void adicionarAluno(Aluno a) {
        if (buscarAluno(a.getMatricula()).isPresent()
                || alunos.stream().anyMatch(aluno ->
                        TextoUtils.equivalentes(aluno.getMatricula(), a.getMatricula())))
            throw NegocioException.conflito("Este aluno já está vinculado à turma.");
        alunos.add(a);
        a.setTurma(this);
    }

    public void removerAluno(Aluno a) {
        alunos.removeIf(x -> x.getMatricula().equals(a.getMatricula()));
        a.setTurma(null);
    }

    public Optional<Aluno> buscarAluno(String matricula) {
        return alunos.stream().filter(a -> a.getMatricula().equals(matricula)).findFirst();
    }

    public BigDecimal calcularMediaTurma() {
        var medias = alunos.stream().filter(Aluno::temNotas).map(Aluno::calcularMedia).toList();
        if (medias.isEmpty()) return BigDecimal.ZERO;
        return medias.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(medias.size()), 2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Turma " + identificador + " (" + alunos.size() + " alunos, média " + calcularMediaTurma() + ")";
    }
}
