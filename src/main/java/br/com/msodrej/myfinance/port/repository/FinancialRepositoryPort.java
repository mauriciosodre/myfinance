package br.com.msodrej.myfinance.port.repository;

import br.com.msodrej.myfinance.domain.model.Financial;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FinancialRepositoryPort {

  Financial save(Financial financial);

  Optional<Financial> findById(Long id);

  Page<Financial> findAll(Financial financial, Pageable pageable);

  void deleteById(Long id);

}
