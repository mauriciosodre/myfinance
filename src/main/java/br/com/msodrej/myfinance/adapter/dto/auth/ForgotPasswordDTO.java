package br.com.msodrej.myfinance.adapter.dto.auth;

import jakarta.validation.constraints.NotEmpty;

public record ForgotPasswordDTO(@NotEmpty String username) {
}
