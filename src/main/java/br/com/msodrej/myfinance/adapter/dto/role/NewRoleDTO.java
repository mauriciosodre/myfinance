package br.com.msodrej.myfinance.adapter.dto.role;

import jakarta.validation.constraints.NotBlank;

public record NewRoleDTO(@NotBlank(message = "name is required") String name) {

}
