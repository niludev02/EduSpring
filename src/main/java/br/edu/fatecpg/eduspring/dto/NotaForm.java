package br.edu.fatecpg.eduspring.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotaForm {
    @NotNull(message = "Escolha a disciplina")
    private Long disciplinaId;

    @NotNull(message = "Informe a nota")
    @DecimalMin(value = "0", message = "A nota mínima é 0")
    @DecimalMax(value = "10", message = "A nota máxima é 10")
    private BigDecimal valor;
}
