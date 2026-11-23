package br.edu.fatecpg.eduspring.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TextoUtilsTest {
    @Test
    void normalizaCaixaEspacosEAcentos() {
        assertEquals("dsm1", TextoUtils.normalizar("  DSm   1 "));
        assertTrue(TextoUtils.equivalentes("Matemática", "  MATEMATICA "));
        assertTrue(TextoUtils.equivalentes("2026 001", "2026001"));
    }

    @Test
    void limpaEspacosNasPontasERepetidos() {
        assertEquals("Ana Maria Souza", TextoUtils.limpar("  Ana   Maria  Souza  "));
    }
}
