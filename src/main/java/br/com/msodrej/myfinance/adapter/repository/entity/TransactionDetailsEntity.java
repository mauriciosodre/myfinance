package br.com.msodrej.myfinance.adapter.repository.entity;

import br.com.msodrej.myfinance.domain.enums.PaymentMethod;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailsEntity {

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;
  private String incomeSource;
  private Boolean recurring;
  private LocalDate dueDate;
}