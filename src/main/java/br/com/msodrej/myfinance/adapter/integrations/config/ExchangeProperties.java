package br.com.msodrej.myfinance.adapter.integrations.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ExchangeProperties {

  @Value("${integrations.exchange-rates.url}")
  private String url;
  @Value("${integrations.exchange-rates.api-key}")
  private String apiKey;
  @Value("${integrations.exchange-rates.endpoints.convert}")
  private String convertEndpoint;

}
