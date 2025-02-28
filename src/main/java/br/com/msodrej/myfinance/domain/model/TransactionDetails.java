package br.com.msodrej.myfinance.domain.model;

import br.com.msodrej.myfinance.domain.enums.PaymentMethod;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetails {

  private PaymentMethod paymentMethod;
  private String incomeSource;
  private Boolean recurring;
  private LocalDate dueDate;
}