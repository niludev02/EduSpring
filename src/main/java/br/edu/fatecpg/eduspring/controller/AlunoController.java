package br.edu.fatecpg.eduspring.controller;

import br.edu.fatecpg.eduspring.dto.AlunoRequest;
import br.edu.fatecpg.eduspring.dto.AlunoResponse;
import br.edu.fatecpg.eduspring.dto.BoletimItem;
import br.edu.fatecpg.eduspring.dto.BoletimResponse;
import br.edu.fatecpg.eduspring.dto.NotaRequest;
import br.edu.fatecpg.eduspring.service.AlunoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class  AlunoController {
    private final AlunoService service;

    @GetMapping
    public List<AlunoResponse> listar(@RequestParam(required = false) String nome,
                                      @RequestParam(required = false) String turma,
                                      @RequestParam(required = false) String ordem) {
        return service.listar(nome, turma, ordem);
    }

    @GetMapping("/matricula/{matricula}")
    public AlunoResponse porMatricula(@PathVariable String matricula) { return service.buscarPorMatricula(matricula); }

    @PostMapping
    public ResponseEntity<AlunoResponse> criar(@Valid @RequestBody AlunoRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(r));
    }

    @PutMapping("/{id}")
    public AlunoResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlunoRequest r) {
        return service.atualizar(id, r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/boletim")
    public BoletimResponse boletim(@PathVariable Long id) { return service.boletim(id); }

    @PutMapping("/{id}/notas/{disciplinaId}")
    public BoletimItem lancarNota(@PathVariable Long id, @PathVariable Long disciplinaId,
                                  @Valid @RequestBody NotaRequest r) {
        return service.lancarNota(id, disciplinaId, r.valor());
    }

    @DeleteMapping("/{id}/notas/{disciplinaId}")
    public ResponseEntity<Void> removerNota(@PathVariable Long id, @PathVariable Long disciplinaId) {
        service.removerNota(id, disciplinaId);
        return ResponseEntity.noContent().build();
    }
}
