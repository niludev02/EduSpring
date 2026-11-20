package br.edu.fatecpg.eduspring.model;

import br.edu.fatecpg.eduspring.exception.NegocioException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Aluno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id")
    private Turma turma;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();

    public Nota adicionarDisciplina(Disciplina d) {
        if (notas.stream().anyMatch(n -> n.getDisciplina().getId().equals(d.getId())))
            throw NegocioException.conflito("Aluno já possui a disciplina " + d.getNome());
        Nota n = new Nota(this, d);
        notas.add(n);
        return n;
    }

    public void removerDisciplina(Disciplina d) {
        boolean removeu = notas.removeIf(n -> n.getDisciplina().getId().equals(d.getId()));
        if (!removeu) throw NegocioException.naoEncontrado("Aluno não possui a disciplina " + d.getNome());
    }

    public boolean temNotas() {
        return notas.stream().anyMatch(n -> n.getValor() != null);
    }

    public BigDecimal calcularMedia() {
        var valores = notas.stream().map(Nota::getValor).filter(Objects::nonNull).toList();
        if (valores.isEmpty()) return BigDecimal.ZERO;
        return valores.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(valores.size()), 2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return matricula + " - " + nome + ", " + idade + " anos, média " + calcularMedia();
    }
}
