package br.edu.fatecpg.eduspring.dto;

import java.math.BigDecimal;
import java.util.List;

public record BoletimResponse(Long alunoId, String nome, BigDecimal media, List<BoletimItem> notas) {}
