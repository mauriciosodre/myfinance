package br.com.msodrej.myfinance.port.repository;

import br.com.msodrej.myfinance.domain.model.CategorySummary;
import br.com.msodrej.myfinance.domain.model.PeriodSummary;
import br.com.msodrej.myfinance.domain.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepositoryPort {

  Transaction save(Transaction transaction);

  void saveAll(List<Transaction> transactions);

  Optional<Transaction> findById(Long id);

  Page<Transaction> findAll(Transaction transaction, Pageable pageable);

  Page<Transaction> findByFinancialAndDateBetween(Long financialId, LocalDate startDate,
      LocalDate endDate, Pageable pageable);

  List<Transaction> findByFinancialAndDateBetween(Long financialId, LocalDate startDate,
      LocalDate endDate);

  void deleteById(Long id);

  BigDecimal calculateBalanceByFinancialId(Long financialId);

  BigDecimal calculateBalanceByFinancialIdAndPeriod(Long financialId, LocalDate startDate,
      LocalDate endDate);

  PeriodSummary calculatePeriodSummaryByFinancialId(Long financialId, LocalDate startDate,
      LocalDate endDate);

  List<CategorySummary> calculateExpensesByCategory(Long financialId, LocalDate startDate,
      LocalDate endDate);

  List<Object[]> calculateIncomesByCategory(Long financialId, LocalDate startDate,
      LocalDate endDate);


}
