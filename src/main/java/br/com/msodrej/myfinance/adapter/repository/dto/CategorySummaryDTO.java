package br.com.msodrej.myfinance.adapter.repository.dto;

import java.math.BigDecimal;

public record CategorySummaryDTO(String name, String color, BigDecimal expenses) {

}