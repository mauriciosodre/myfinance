package br.com.msodrej.myfinance.adapter.repository.mapper;

import br.com.msodrej.myfinance.adapter.repository.entity.FinancialEntity;
import br.com.msodrej.myfinance.domain.model.Financial;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinancialEntityMapper {

  Financial toModel(FinancialEntity financialEntity);

  FinancialEntity toEntity(Financial financial);

}
