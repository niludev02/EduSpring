package br.edu.fatecpg.eduspring.util;

import java.text.Normalizer;
import java.util.Locale;

public final class TextoUtils {
    private TextoUtils() {}

    public static String limpar(String texto) {
        if (texto == null) return null;
        return texto.trim().replaceAll("\\s+", " ");
    }

    public static String normalizar(String texto) {
        String limpo = limpar(texto);
        if (limpo == null) return "";
        String semAcentos = Normalizer.normalize(limpo, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return semAcentos.toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
    }

    public static boolean equivalentes(String primeiro, String segundo) {
        return normalizar(primeiro).equals(normalizar(segundo));
    }
}
