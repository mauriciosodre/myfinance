package br.com.msodrej.myfinance.adapter.integrations.dto;

import java.math.BigDecimal;

public record InforResponseDTO(
    Long timestamp,
    BigDecimal rate
) {

}
