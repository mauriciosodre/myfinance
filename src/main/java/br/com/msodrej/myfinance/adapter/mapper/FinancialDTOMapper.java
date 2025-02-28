package br.com.msodrej.myfinance.adapter.mapper;

import br.com.msodrej.myfinance.adapter.dto.financial.FinancialPayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.financial.FinancialResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.financial.NewFinancialDTO;
import br.com.msodrej.myfinance.domain.model.Financial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FinancialDTOMapper {

  @Mapping(target = "owner.id", source = "ownerId")
  Financial toModel(NewFinancialDTO dto);

  @Mapping(target = "owner.id", source = "ownerId")
  Financial toModel(FinancialPayloadDTO dto);


  @Mapping(target = "ownerId", source = "owner.id")
  FinancialResponseDTO toDTO(Financial financial);

}
