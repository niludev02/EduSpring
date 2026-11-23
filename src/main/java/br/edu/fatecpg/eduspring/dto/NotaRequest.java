package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record NotaRequest(
        @NotNull(message = "Informe a nota")
        @DecimalMin(value = "0", message = "A nota não pode ser menor que 0")
        @DecimalMax(value = "10", message = "A nota não pode ser maior que 10")
        BigDecimal valor) {}
