package br.com.msodrej.myfinance.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeriodSummary {

  private BigDecimal totalIncome;
  private BigDecimal totalExpense;

  public BigDecimal getBalance() {
    return totalIncome.subtract(totalExpense);
  }
}