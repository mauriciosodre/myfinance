package br.com.msodrej.myfinance.adapter.dto.financial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FinancialPayloadDTO(@NotNull(message = "id is required") Long id,
                                  @NotBlank(message = "name is required") String name,
                                  String description,
                                  @NotNull(message = "ownerId is required") UUID ownerId) {

}
