package br.com.msodrej.myfinance.domain.model;

import br.com.msodrej.myfinance.domain.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

  private Long id;

  private BigDecimal amount;
  private String description;
  private LocalDate date;

  private TransactionType type;

  private Financial financial;

  private TransactionDetails details;
}

