package br.com.msodrej.myfinance.adapter.repository.operations;

import br.com.msodrej.myfinance.adapter.repository.mapper.TransactionEntityMapper;
import br.com.msodrej.myfinance.adapter.repository.repositories.TransactionRepository;
import br.com.msodrej.myfinance.domain.exceptions.DatabaseErrorException;
import br.com.msodrej.myfinance.domain.model.CategorySummary;
import br.com.msodrej.myfinance.domain.model.PeriodSummary;
import br.com.msodrej.myfinance.domain.model.Transaction;
import br.com.msodrej.myfinance.port.repository.TransactionRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Transaction save(Transaction transaction) {
    try {
      return mapper.toModel(transactionRepository.save(mapper.toEntity(transaction)));
    } catch (Exception e) {
      throw new DatabaseErrorException("Error saving transaction, cause: " + e.getMessage());
    }
  }

  @Override
  public void saveAll(List<Transaction> transactions) {
    try {
      var transactionEntities = transactions.stream()
          .map(mapper::toEntity)
          .toList();
      transactionRepository.saveAll(transactionEntities);
    }catch (Exception e) {
      throw new DatabaseErrorException("Error saving transactions, cause: " + e.getMessage());
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
  public Page<Transaction> findByFinancialAndDateBetween(Long financialId, LocalDate startDate,
      LocalDate endDate, Pageable pageable) {
    return transactionRepository.findByFinancialIdAndDateBetweenOrderByDateDesc(financialId,
        startDate, endDate,
        pageable).map(mapper::toModel);
  }

  @Override
  public List<Transaction> findByFinancialAndDateBetween(Long financialId, LocalDate startDate,
      LocalDate endDate) {
    return transactionRepository.findByFinancialIdAndDateBetweenOrderByDateDesc(financialId,
            startDate, endDate)
        .stream()
        .map(mapper::toModel).toList();
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

  @Override
  public BigDecimal calculateBalanceByFinancialId(Long financialId) {
    String jpql =
        "SELECT COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END), 0) " +
        "FROM TransactionEntity t WHERE t.financial.id = :financialId";

    var query = entityManager.createQuery(jpql);
    query.setParameter("financialId", financialId);

    Object result = query.getSingleResult();
    return result != null ? (BigDecimal) result : BigDecimal.ZERO;
  }

  @Override
  public BigDecimal calculateBalanceByFinancialIdAndPeriod(Long financialId, LocalDate startDate,
      LocalDate endDate) {
    String jpql =
        "SELECT COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END), 0) " +
        "FROM TransactionEntity t " +
        "WHERE t.financial.id = :financialId " +
        "AND t.date BETWEEN :startDate AND :endDate";

    var query = entityManager.createQuery(jpql);
    query.setParameter("financialId", financialId);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    Object result = query.getSingleResult();
    return result != null ? (BigDecimal) result : BigDecimal.ZERO;
  }

  @Override
  public PeriodSummary calculatePeriodSummaryByFinancialId(Long financialId, LocalDate startDate,
      LocalDate endDate) {
    String jpql = "SELECT " +
                  "COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), 0), " +
                  "COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0) " +
                  "FROM TransactionEntity t " +
                  "WHERE t.financial.id = :financialId " +
                  "AND t.date BETWEEN :startDate AND :endDate";

    var query = entityManager.createQuery(jpql);
    query.setParameter("financialId", financialId);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    Object[] results = (Object[]) query.getSingleResult();

    return PeriodSummary.builder()
        .totalIncome((BigDecimal) results[0])
        .totalExpense((BigDecimal) results[1])
        .build();
  }

  @Override
  public List<CategorySummary> calculateExpensesByCategory(Long financialId, LocalDate startDate,
      LocalDate endDate) {
    String jpql =
        "SELECT new br.com.msodrej.myfinance.adapter.repository.dto.CategorySummaryDTO(t.category.name, SUM(t.amount)) "
        +
        "FROM TransactionEntity t " +
        "WHERE t.financial.id = :financialId " +
        "AND t.type = 'EXPENSE' " +
        "AND t.date BETWEEN :startDate AND :endDate " +
        "GROUP BY t.category " +
        "ORDER BY SUM(t.amount) DESC";

    var query = entityManager.createQuery(jpql, CategorySummary.class);
    query.setParameter("financialId", financialId);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    return query.getResultList();
  }

  @Override
  public List<Object[]> calculateIncomesByCategory(Long financialId, LocalDate startDate,
      LocalDate endDate) {
    String jpql = "SELECT t.description, SUM(t.amount) " +
                  "FROM TransactionEntity t " +
                  "WHERE t.financial.id = :financialId " +
                  "AND t.type = 'INCOME' " +
                  "AND t.date BETWEEN :startDate AND :endDate " +
                  "GROUP BY t.description";

    var query = entityManager.createQuery(jpql);
    query.setParameter("financialId", financialId);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    return query.getResultList();
  }
}