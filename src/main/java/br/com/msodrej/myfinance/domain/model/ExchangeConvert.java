package br.com.msodrej.myfinance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeConvert {

  private Boolean success;
  private Query query;
  private Infor info;
  private String historical;
  private LocalDate date;
  private BigDecimal result;

}
