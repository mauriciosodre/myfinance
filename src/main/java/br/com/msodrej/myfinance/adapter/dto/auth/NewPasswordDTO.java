package br.com.msodrej.myfinance.adapter.dto.auth;

import jakarta.validation.constraints.NotEmpty;

public record NewPasswordDTO(@NotEmpty String oldPassword, @NotEmpty String newPassword) {

}
