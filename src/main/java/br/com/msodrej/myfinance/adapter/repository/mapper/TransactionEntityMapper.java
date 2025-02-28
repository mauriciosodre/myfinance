package br.com.msodrej.myfinance.adapter.repository.mapper;

import br.com.msodrej.myfinance.adapter.repository.entity.TransactionEntity;
import br.com.msodrej.myfinance.domain.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionEntityMapper {

  Transaction toModel(TransactionEntity transactionEntity);

  TransactionEntity toEntity(Transaction transaction);

}
