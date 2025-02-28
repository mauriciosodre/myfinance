package br.com.msodrej.myfinance.adapter.repository.repositories;

import br.com.msodrej.myfinance.adapter.repository.entity.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialRepository extends JpaRepository<FinancialEntity, Long> {

}
