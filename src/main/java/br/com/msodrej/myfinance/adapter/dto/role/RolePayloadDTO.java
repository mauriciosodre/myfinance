package br.com.msodrej.myfinance.adapter.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RolePayloadDTO(@NotNull(message = "id is required") Long id,
                             @NotBlank(message = "name is required") String name) {

}
