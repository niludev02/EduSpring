package br.edu.fatecpg.eduspring.repository;

import br.edu.fatecpg.eduspring.model.Aluno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query(value = """
            SELECT COUNT(*) FROM aluno a
            JOIN turma t ON t.id = a.turma_id
            WHERE t.identificador = :identificador
            """, nativeQuery = true)
    long contarAlunosPorTurma(@Param("identificador") String identificador);

    Optional<Aluno> findByMatricula(String matricula);
    List<Aluno> findByNomeContainingIgnoreCase(String nome);
    List<Aluno> findByTurmaIdentificador(String identificador);

    @Query("SELECT a FROM Aluno a ORDER BY a.idade")
    List<Aluno> listarOrdenadosPorIdade();

    @Query("SELECT a FROM Aluno a LEFT JOIN a.notas n GROUP BY a ORDER BY AVG(n.valor) DESC NULLS LAST")
    List<Aluno> listarOrdenadosPorMedia();
}
