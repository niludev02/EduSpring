package br.edu.fatecpg.eduspring.controller;

import br.edu.fatecpg.eduspring.dto.DisciplinaRequest;
import br.edu.fatecpg.eduspring.dto.DisciplinaResponse;
import br.edu.fatecpg.eduspring.service.DisciplinaService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/disciplinas")
@RequiredArgsConstructor
public class DisciplinaController {
    private final DisciplinaService service;

    @GetMapping
    public List<DisciplinaResponse> listar() { return service.listar(); }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> criar(@Valid @RequestBody DisciplinaRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(r));
    }
}
