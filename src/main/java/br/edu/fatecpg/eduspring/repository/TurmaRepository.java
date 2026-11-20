package br.edu.fatecpg.eduspring.repository;

import br.edu.fatecpg.eduspring.model.Turma;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    Optional<Turma> findByIdentificador(String identificador);
}
