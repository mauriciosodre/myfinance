package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR005;

import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Transaction;
import br.com.msodrej.myfinance.port.repository.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionUseCase {

  private final TransactionRepositoryPort transactionRepository;

  public Transaction save(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  public Transaction findById(Long id) {
    return transactionRepository.findById(id).orElseThrow(() -> new SystemErrorException(
        ERR005.getFormattedMessage(id)));
  }

  public Page<Transaction> findAll(Transaction transaction, Pageable pageable) {
    return transactionRepository.findAll(transaction, pageable);
  }

  public void deleteTransaction(Long id) {
    var transaction = findById(id);
    transactionRepository.deleteById(transaction.getId());
  }

}
