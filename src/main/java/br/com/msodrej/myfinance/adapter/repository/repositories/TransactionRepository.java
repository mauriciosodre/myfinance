package br.com.msodrej.myfinance.adapter.repository.repositories;

import br.com.msodrej.myfinance.adapter.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

}
