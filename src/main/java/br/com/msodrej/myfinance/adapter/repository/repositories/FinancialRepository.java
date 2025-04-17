package br.com.msodrej.myfinance.adapter.repository.repositories;

import br.com.msodrej.myfinance.adapter.repository.entity.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FinancialRepository extends JpaRepository<FinancialEntity, Long>,
    JpaSpecificationExecutor<FinancialEntity> {

}