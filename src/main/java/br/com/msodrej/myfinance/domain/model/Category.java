package br.com.msodrej.myfinance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

  private Long id;
  private String name;
  private String description;
  private String color;

  private Boolean enabled;

  private Boolean deleted;

}