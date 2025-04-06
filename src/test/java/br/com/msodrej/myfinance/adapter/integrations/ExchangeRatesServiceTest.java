package br.com.msodrej.myfinance.adapter.integrations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;

import br.com.msodrej.myfinance.adapter.integrations.config.ExchangeProperties;
import br.com.msodrej.myfinance.adapter.integrations.dto.ExchangeConvertResponseDTO;
import br.com.msodrej.myfinance.adapter.integrations.dto.InforResponseDTO;
import br.com.msodrej.myfinance.adapter.integrations.dto.QueryResponseDTO;
import br.com.msodrej.myfinance.adapter.integrations.mapper.ExchangeConvertMapperDTO;
import br.com.msodrej.myfinance.domain.model.ExchangeConvert;
import br.com.msodrej.myfinance.domain.model.Infor;
import br.com.msodrej.myfinance.domain.model.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
class ExchangeRatesServiceTest {

  @Mock
  private ExchangeProperties properties;
  @Mock
  private ExchangeConvertMapperDTO mapper;
  @Mock
  private RestTemplate restTemplate;
  @InjectMocks
  private ExchangeRatesService exchangeRatesService;

  @Test
  void convertExchange() {
    String from = "USD";
    String to = "BRL";
    BigDecimal amount = BigDecimal.valueOf(100);

    BigDecimal rate = BigDecimal.valueOf(new Random().nextDouble() * 10);

    var responseDTO = createExchangeConvertResponse(from, to, amount, rate);
    var responseModel = createExchangeConvert(from, to, amount, rate);

    when(properties.getUrl()).thenReturn("http://api.exchangeratesapi.io");
    when(properties.getConvertEndpoint()).thenReturn("/convert");
    when(properties.getApiKey()).thenReturn("test-api-key");
    when(mapper.toModel(any(ExchangeConvertResponseDTO.class))).thenReturn(responseModel);

    when(restTemplate.exchange(anyString(), eq(GET), any(), eq(ExchangeConvertResponseDTO.class)))
        .thenReturn(ResponseEntity.ok(responseDTO));
    when(mapper.toModel(responseDTO)).thenReturn(responseModel);

    var response = exchangeRatesService.convertExchange(from, to, amount);

    assertThat(response).isNotNull();
    assertThat(response.getResult()).isEqualTo(
        amount.multiply(rate).setScale(2, RoundingMode.HALF_UP));
  }

  private ExchangeConvertResponseDTO createExchangeConvertResponse(String from, String to,
      BigDecimal amount, BigDecimal rate) {
    return new ExchangeConvertResponseDTO(true, new QueryResponseDTO(from, to, amount),
        new InforResponseDTO(System.currentTimeMillis(), rate), "",
        LocalDate.parse("2025-03-31"), amount.multiply(rate).setScale(2, RoundingMode.HALF_UP));
  }

  private ExchangeConvert createExchangeConvert(String from, String to,
      BigDecimal amount, BigDecimal rate) {
    return new ExchangeConvert(true, new Query(from, to, amount),
        new Infor(rate, System.currentTimeMillis()), "",
        LocalDate.parse("2025-03-31"), amount.multiply(rate).setScale(2, RoundingMode.HALF_UP));
  }

}
