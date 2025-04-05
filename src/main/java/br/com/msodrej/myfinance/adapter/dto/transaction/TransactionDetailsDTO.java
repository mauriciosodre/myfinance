package br.com.msodrej.myfinance.adapter.dto.transaction;

import br.com.msodrej.myfinance.domain.enums.PaymentMethod;
import java.time.LocalDate;

public record TransactionDetailsDTO(
    PaymentMethod paymentMethod,
    String incomeSource,
    Boolean recurring,
    LocalDate dueDate
) {}