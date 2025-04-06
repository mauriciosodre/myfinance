package br.com.msodrej.myfinance.port.integrations;

import br.com.msodrej.myfinance.domain.model.ExchangeConvert;
import java.math.BigDecimal;

public interface ExchangeRatesApiPort {

  ExchangeConvert convertExchange(String from, String to, BigDecimal amount);

}
