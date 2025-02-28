package br.com.msodrej.myfinance.adapter.repository.operations;

import br.com.msodrej.myfinance.adapter.repository.mapper.TransactionEntityMapper;
import br.com.msodrej.myfinance.adapter.repository.repositories.TransactionRepository;
import br.com.msodrej.myfinance.domain.exceptions.DatabaseErrorException;
import br.com.msodrej.myfinance.domain.model.Transaction;
import br.com.msodrej.myfinance.port.repository.TransactionRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionOperations implements TransactionRepositoryPort {

  private final TransactionRepository transactionRepository;
  private final TransactionEntityMapper mapper;

  @Override
  public Transaction save(Transaction transaction) {
    try {
      return mapper.toModel(transactionRepository.save(mapper.toEntity(transaction)));
    } catch (Exception e) {
      throw new DatabaseErrorException("Error saving transaction, cause: " + e.getMessage());
    }
  }

  @Override
  public Optional<Transaction> findById(Long id) {
    try {
      return transactionRepository.findById(id).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error finding transaction by Id, cause: " + e.getMessage());
    }
  }

  @Override
  public Page<Transaction> findAll(Transaction transaction, Pageable pageable) {
    try {
      return transactionRepository.findAll(pageable).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error finding transactions, cause: " + e.getMessage());
    }
  }

  @Override
  public void deleteById(Long id) {
    try {
      transactionRepository.deleteById(id);
    } catch (Exception e) {
      throw new DatabaseErrorException(
          "Error deleting transaction by Id, cause: " + e.getMessage());
    }
  }
}
