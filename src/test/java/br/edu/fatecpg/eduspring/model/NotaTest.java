package br.edu.fatecpg.eduspring.model;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.fatecpg.eduspring.exception.NegocioException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class NotaTest {

    @Test
    void aceitaLimites() {
        Nota n = new Nota();
        n.atribuirNota(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, n.getValor());
        n.atribuirNota(BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, n.getValor());
    }

    @Test
    void rejeitaNotaForaDoIntervalo() {
        Nota n = new Nota();
        assertThrows(NegocioException.class, () -> n.atribuirNota(new BigDecimal("10.01")));
        assertThrows(NegocioException.class, () -> n.atribuirNota(new BigDecimal("-0.1")));
        assertThrows(NegocioException.class, () -> n.atribuirNota(null));
    }
}
