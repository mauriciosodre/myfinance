package br.com.msodrej.myfinance.adapter.repository.entity;

import static jakarta.persistence.GenerationType.SEQUENCE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class CategoryEntity {

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "category_id_seq")
  @SequenceGenerator(name = "category_id_seq", sequenceName = "category_seq", allocationSize = 1)
  private Long id;

  private String name;
  private String description;
  private String color;

  @Column(nullable = false)
  private Boolean enabled;

  @Column(nullable = false)
  private Boolean deleted;

  @PrePersist
  public void prePersist() {
    if (enabled == null) {
      enabled = true;
    }
    if (deleted == null) {
      deleted = false;
    }
  }

}