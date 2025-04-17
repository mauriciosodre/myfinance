package br.com.msodrej.myfinance.adapter.mapper;

import br.com.msodrej.myfinance.adapter.dto.transaction.NewTransactionDTO;
import br.com.msodrej.myfinance.adapter.dto.transaction.TransactionPayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.transaction.TransactionResponseDTO;
import br.com.msodrej.myfinance.domain.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionDTOMapper {

  @Mapping(target = "financial.id", source = "financialId")
  @Mapping(target = "details", source = "details") // Add mapping for details
  @Mapping(target = "category.id", source = "categoryId")
  Transaction toModel(NewTransactionDTO dto);

  @Mapping(target = "financial.id", source = "financialId")
  @Mapping(target = "details", source = "details")
  @Mapping(target = "category.id", source = "categoryId")
  Transaction toModel(TransactionPayloadDTO dto);

  @Mapping(target = "financial", source = "financial")
  @Mapping(target = "details", source = "details")
    // Add mapping for details
  TransactionResponseDTO toDTO(Transaction transaction);
}