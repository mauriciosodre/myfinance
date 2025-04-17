package br.com.msodrej.myfinance.adapter.dto.transaction;

import br.com.msodrej.myfinance.adapter.dto.category.CategoryResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.financial.FinancialResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.user.UserResponseDTO;
import br.com.msodrej.myfinance.domain.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(
    Long id,
    BigDecimal amount,
    String description,
    LocalDate date,
    TransactionType type,
    FinancialResponseDTO financial,
    UserResponseDTO user,
    TransactionDetailsDTO details,
    CategoryResponseDTO category
) {

}