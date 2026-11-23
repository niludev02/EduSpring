package br.edu.fatecpg.eduspring.controller;

import br.edu.fatecpg.eduspring.dto.TurmaRequest;
import br.edu.fatecpg.eduspring.dto.TurmaResponse;
import br.edu.fatecpg.eduspring.service.TurmaService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
public class TurmaController {
    private final TurmaService service;

    @GetMapping
    public List<TurmaResponse> listar() { return service.listar(); }

    @PostMapping
    public ResponseEntity<TurmaResponse> criar(@Valid @RequestBody TurmaRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(r));
    }

    @GetMapping("/{id}")
    public TurmaResponse buscar(@PathVariable Long id) { return service.buscarPorId(id); }

    @PutMapping("/{id}")
    public TurmaResponse atualizar(@PathVariable Long id, @Valid @RequestBody TurmaRequest r) {
        return service.atualizar(id, r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/media")
    public BigDecimal media(@PathVariable Long id) { return service.media(id); }

    @GetMapping("/{identificador}/quantidade-alunos")
    public long quantidade(@PathVariable String identificador) { return service.quantidadeAlunos(identificador); }
}
