package br.com.msodrej.myfinance.domain.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Auditable {

  private Boolean active;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String createdBy;

  private String updatedBy;

}
