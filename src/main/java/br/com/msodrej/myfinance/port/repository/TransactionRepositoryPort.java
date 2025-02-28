package br.com.msodrej.myfinance.port.repository;

import br.com.msodrej.myfinance.domain.model.Transaction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepositoryPort {

  Transaction save(Transaction transaction);

  Optional<Transaction> findById(Long id);

  Page<Transaction> findAll(Transaction transaction, Pageable pageable);

  void deleteById(Long id);

}
