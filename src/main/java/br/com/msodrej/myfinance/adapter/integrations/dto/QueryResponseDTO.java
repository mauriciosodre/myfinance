package br.com.msodrej.myfinance.adapter.integrations.dto;

import java.math.BigDecimal;

public record QueryResponseDTO(
    String from,
    String to,
    BigDecimal amount
) {

}
