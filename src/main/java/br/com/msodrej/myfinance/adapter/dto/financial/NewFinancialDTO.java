package br.com.msodrej.myfinance.adapter.dto.financial;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record NewFinancialDTO(
  @NotBlank(message = "name is required") String name,
  String description,
  UUID ownerId
) {

}
