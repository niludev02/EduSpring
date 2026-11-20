package br.edu.fatecpg.eduspring.dto;

import java.math.BigDecimal;

public record BoletimItem(Long disciplinaId, String disciplina, BigDecimal valor) {}
