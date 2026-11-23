package br.edu.fatecpg.eduspring.exception;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ErroHandler {
    private static final Map<String, String> CAMPOS = Map.of(
            "matricula", "Matrícula",
            "nome", "Nome",
            "idade", "Idade",
            "identificador", "Identificador",
            "disciplinaId", "Disciplina",
            "valor", "Nota"
    );

    public record Erro(int status, String mensagem, List<String> detalhes) {}

    @ExceptionHandler(NegocioException.class)
    ResponseEntity<Erro> negocio(NegocioException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new Erro(e.getStatus().value(), e.getMessage(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Erro> validacao(MethodArgumentNotValidException e) {
        var detalhes = e.getBindingResult().getFieldErrors().stream()
                .map(f -> CAMPOS.getOrDefault(f.getField(), f.getField()) + ": " + f.getDefaultMessage())
                .distinct()
                .toList();
        return ResponseEntity.badRequest()
                .body(new Erro(400, "Não foi possível salvar: confira os dados informados.", detalhes));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Erro> integridade(DataIntegrityViolationException e) {
        return ResponseEntity.status(409)
                .body(new Erro(409,
                        "Não foi possível concluir a operação. O registro já existe ou está sendo utilizado.",
                        List.of()));
    }
}
