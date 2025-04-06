package br.com.msodrej.myfinance.adapter.integrations;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import br.com.msodrej.myfinance.adapter.integrations.config.ExchangeProperties;
import br.com.msodrej.myfinance.adapter.integrations.dto.ExchangeConvertResponseDTO;
import br.com.msodrej.myfinance.adapter.integrations.mapper.ExchangeConvertMapperDTO;
import br.com.msodrej.myfinance.domain.exceptions.IntegrationErrorException;
import br.com.msodrej.myfinance.domain.model.ExchangeConvert;
import br.com.msodrej.myfinance.port.integrations.ExchangeRatesApiPort;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class ExchangeRatesService implements ExchangeRatesApiPort {

  private final ExchangeProperties exchangeProperties;
  private final ExchangeConvertMapperDTO mapper;

  @Override
  public ExchangeConvert convertExchange(String from, String to, BigDecimal amount) {
    try {

      var restTemplate = new RestTemplate();
      var result = restTemplate.exchange(getUriComponentsBuilder(from, to, amount).toUriString(),
          HttpMethod.GET,
          null, ExchangeConvertResponseDTO.class);
      return mapper.toModel(result.getBody());
    } catch (Exception e) {
      throw new IntegrationErrorException("Error converting exchange: " + e.getMessage());
    }
  }

  private UriComponentsBuilder getUriComponentsBuilder(String from, String to, BigDecimal amount) {
    return fromHttpUrl(exchangeProperties.getUrl())
        .path(exchangeProperties.getConvertEndpoint())
        .queryParam("accessKey", exchangeProperties.getApiKey())
        .queryParam("from", from)
        .queryParam("to", to)
        .queryParam("amount", amount);
  }
}
