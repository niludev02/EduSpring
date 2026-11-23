package br.edu.fatecpg.eduspring.model;

import br.edu.fatecpg.eduspring.exception.NegocioException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor
public class Nota {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 4, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    public Nota(Aluno aluno, Disciplina disciplina) {
        this.aluno = aluno;
        this.disciplina = disciplina;
    }

    public void atribuirNota(BigDecimal novaNota) {
        if (novaNota == null || novaNota.compareTo(BigDecimal.ZERO) < 0 || novaNota.compareTo(BigDecimal.TEN) > 0)
            throw NegocioException.invalido("Informe uma nota entre 0 e 10.");
        this.valor = novaNota;
    }
}
