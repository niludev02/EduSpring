package br.edu.fatecpg.eduspring.exception;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ErroHandler {

    public record Erro(int status, String mensagem, List<String> detalhes) {}

    @ExceptionHandler(NegocioException.class)
    ResponseEntity<Erro> negocio(NegocioException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new Erro(e.getStatus().value(), e.getMessage(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Erro> validacao(MethodArgumentNotValidException e) {
        var detalhes = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage()).toList();
        return ResponseEntity.badRequest().body(new Erro(400, "Dados inválidos", detalhes));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Erro> integridade(DataIntegrityViolationException e) {
        return ResponseEntity.status(409)
                .body(new Erro(409, "Operação viola uma restrição do banco (registro duplicado ou em uso)", List.of()));
    }
}
