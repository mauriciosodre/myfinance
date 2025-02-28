package br.com.msodrej.myfinance.adapter.repository.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.SEQUENCE;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "financials")
public class FinancialEntity {

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "financial_id_seq")
  @SequenceGenerator(name = "financial_id_seq", sequenceName = "financial_seq", allocationSize = 1)
  private Long id;

  private String name;
  private String description;
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false)
  private UserEntity owner;

  @ManyToMany
  @JoinTable(
      name = "financial_shares",
      joinColumns = @JoinColumn(name = "financial_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private Set<UserEntity> sharedWith = new HashSet<>();

  @OneToMany(mappedBy = "financial", cascade = ALL, orphanRemoval = true)
  private List<TransactionEntity> transactions = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}