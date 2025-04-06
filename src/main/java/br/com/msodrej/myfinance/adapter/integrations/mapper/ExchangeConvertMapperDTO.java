package br.com.msodrej.myfinance.adapter.integrations.mapper;

import br.com.msodrej.myfinance.adapter.integrations.dto.ExchangeConvertResponseDTO;
import br.com.msodrej.myfinance.domain.model.ExchangeConvert;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeConvertMapperDTO {

  ExchangeConvert toModel(ExchangeConvertResponseDTO dto);

}
