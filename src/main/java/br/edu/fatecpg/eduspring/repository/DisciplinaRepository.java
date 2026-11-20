package br.edu.fatecpg.eduspring.repository;

import br.edu.fatecpg.eduspring.model.Disciplina;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    Optional<Disciplina> findByNome(String nome);
}
