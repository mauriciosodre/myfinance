package br.com.msodrej.myfinance.adapter.repository.entity;

import static jakarta.persistence.GenerationType.SEQUENCE;

import br.com.msodrej.myfinance.domain.enums.TransactionType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "transactions")
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "transaction_id_seq")
  @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_seq", allocationSize = 1)
  private Long id;

  private BigDecimal amount;
  private String description;
  private LocalDate date;

  @Enumerated(EnumType.STRING)
  private TransactionType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "transaction_id", nullable = false)
  private FinancialEntity financial;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Embedded
  private TransactionDetailsEntity details;
}

