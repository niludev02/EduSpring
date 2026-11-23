package br.edu.fatecpg.eduspring.repository;

import br.edu.fatecpg.eduspring.model.Disciplina;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    Optional<Disciplina> findByNome(String nome);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM nota WHERE disciplina_id = :id)", nativeQuery = true)
    boolean possuiNotas(@Param("id") Long id);
}
