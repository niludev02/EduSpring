package br.edu.fatecpg.eduspring.controller;

import br.edu.fatecpg.eduspring.dto.ProfessorRequest;
import br.edu.fatecpg.eduspring.dto.ProfessorResponse;
import br.edu.fatecpg.eduspring.service.ProfessorService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professores")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorService service;

    @GetMapping
    public List<ProfessorResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ProfessorResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProfessorResponse> criar(@Valid @RequestBody ProfessorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @PutMapping("/{id}")
    public ProfessorResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProfessorRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
