package br.com.msodrej.myfinance.adapter.integrations.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExchangeConvertResponseDTO(
    Boolean success,
    QueryResponseDTO query,
    InforResponseDTO info,
    String historical,
    LocalDate date,
    BigDecimal result
) {

}
