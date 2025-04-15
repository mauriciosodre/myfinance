package br.com.msodrej.myfinance.adapter.repository.repositories;

import br.com.msodrej.myfinance.adapter.repository.dto.PeriodSummaryDTO;
import br.com.msodrej.myfinance.adapter.repository.entity.TransactionEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

  Page<TransactionEntity> findByFinancialIdAndDateBetweenOrderByDateDesc(Long financialId, LocalDate start,
      LocalDate end, Pageable pageable);

  List<TransactionEntity> findByFinancialIdAndDateBetweenOrderByDateDesc(Long financialId, LocalDate start,
      LocalDate end);

  @Query("""
          SELECT COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END), 0)
          FROM TransactionEntity t
          WHERE t.financial.id = :financialId
      """)
  BigDecimal calculateBalanceByFinancialId(@Param("financialId") Long financialId);

  @Query("""
          SELECT COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END), 0)
          FROM TransactionEntity t
          WHERE t.financial.id = :financialId
          AND t.date BETWEEN :startDate AND :endDate
      """)
  BigDecimal calculateBalanceByFinancialIdAndPeriod(
      @Param("financialId") Long financialId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query("""
          SELECT new br.com.msodrej.myfinance.adapter.repository.dto.PeriodSummaryDTO(
              COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE CAST(0 AS bigdecimal ) END), CAST(0 AS bigdecimal )),
              COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE CAST(0 AS bigdecimal ) END), CAST(0 AS bigdecimal ))
          )
          FROM TransactionEntity t
          WHERE t.financial.id = :financialId
          AND t.date BETWEEN :startDate AND :endDate
      """)
  PeriodSummaryDTO calculatePeriodSummary(
      @Param("financialId") Long financialId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);


}
