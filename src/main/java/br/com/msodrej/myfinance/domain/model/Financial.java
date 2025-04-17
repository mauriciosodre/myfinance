package br.com.msodrej.myfinance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Financial {

  private Long id;

  private String name;
  private String description;
  private LocalDateTime createdAt;

  private User owner;

  private Set<User> sharedWith = new HashSet<>();

  //Variables for summary
  private BigDecimal balance;
  private BigDecimal incomes;
  private BigDecimal expenses;
}