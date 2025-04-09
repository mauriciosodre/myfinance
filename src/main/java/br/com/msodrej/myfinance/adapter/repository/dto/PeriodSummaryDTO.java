package br.com.msodrej.myfinance.adapter.repository.dto;

import java.math.BigDecimal;

public record PeriodSummaryDTO(BigDecimal totalIncome, BigDecimal totalExpense) {

}