package br.edu.fatecpg.eduspring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NegocioException extends RuntimeException {
    private final HttpStatus status;

    private NegocioException(HttpStatus status, String msg) {
        super(msg);
        this.status = status;
    }

    public static NegocioException invalido(String msg) { return new NegocioException(HttpStatus.BAD_REQUEST, msg); }
    public static NegocioException naoEncontrado(String msg) { return new NegocioException(HttpStatus.NOT_FOUND, msg); }
    public static NegocioException conflito(String msg) { return new NegocioException(HttpStatus.CONFLICT, msg); }
}
