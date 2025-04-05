package br.com.msodrej.myfinance.adapter.dto.transaction;

import br.com.msodrej.myfinance.domain.enums.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionPayloadDTO(
    @NotNull Long id,
    @NotNull @Positive BigDecimal amount,
    @NotBlank String description,
    @NotNull LocalDate date,
    @NotNull TransactionType type,
    @NotNull Long financialId,
    @Valid TransactionDetailsDTO details
) {

}