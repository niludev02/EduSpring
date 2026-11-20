package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record NotaRequest(@NotNull @DecimalMin("0") @DecimalMax("10") BigDecimal valor) {}
