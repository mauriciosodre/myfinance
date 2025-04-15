package br.com.msodrej.myfinance.adapter.dto.transaction;

import br.com.msodrej.myfinance.domain.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record NewTransactionDTO(
    BigDecimal amount,
    String description,
    LocalDate date,
    TransactionType type,
    Long financialId,
    TransactionDetailsDTO details,
    Long categoryId
) {

}