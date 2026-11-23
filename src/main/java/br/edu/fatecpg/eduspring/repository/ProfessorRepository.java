package br.edu.fatecpg.eduspring.repository;

import br.edu.fatecpg.eduspring.model.Professor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    long countByDisciplinaId(Long disciplinaId);
    Optional<Professor> findByNomeIgnoreCase(String nome);
    List<Professor> findByTurmasId(Long turmaId);

    @Query("SELECT DISTINCT p FROM Professor p LEFT JOIN FETCH p.turmas")
    List<Professor> listarComTurmas();

    @Query(value = "SELECT COUNT(*) FROM professor_turma WHERE turma_id = :turmaId", nativeQuery = true)
    long contarResponsaveisPorTurma(@Param("turmaId") Long turmaId);
}
