package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR005;
import static br.com.msodrej.myfinance.domain.utils.SecurityUtils.getCurrentUser;
import static java.time.format.TextStyle.SHORT;

import br.com.msodrej.myfinance.domain.enums.SystemErrorMessage;
import br.com.msodrej.myfinance.domain.enums.TransactionType;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.CategorySummary;
import br.com.msodrej.myfinance.domain.model.Financial;
import br.com.msodrej.myfinance.domain.model.PeriodSummary;
import br.com.msodrej.myfinance.domain.model.Transaction;
import br.com.msodrej.myfinance.port.repository.TransactionRepositoryPort;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionUseCase {

  private final TransactionRepositoryPort transactionRepository;
  private final FinancialUseCase financialUseCase;

  public Transaction save(Transaction transaction) {
    var financial = financialUseCase.findById(transaction.getFinancial().getId());

    validateUserPermission(financial);

    validateTransactionDetails(transaction);

    if (Objects.isNull(transaction.getCategory().getId())) {
      transaction.setCategory(null);
    }

    return transactionRepository.save(transaction);
  }

  public void saveAll(List<Transaction> transactions, Long financialId) {
    var financial = financialUseCase.findById(financialId);
    validateUserPermission(financial);

    for (Transaction transaction : transactions) {
      validateTransactionDetails(transaction);
      transaction.setFinancial(financial);
      if (Objects.isNull(transaction.getCategory()) || Objects.isNull(
          transaction.getCategory().getId())) {
        transaction.setCategory(null);
      }
    }

    transactionRepository.saveAll(transactions);

  }

  public Transaction findById(Long id) {
    return transactionRepository.findById(id).orElseThrow(() -> new SystemErrorException(
        ERR005.getFormattedMessage(id)));
  }

  public Page<Transaction> findAll(Transaction transaction, Pageable pageable) {
    return transactionRepository.findAll(transaction, pageable);
  }

  public Transaction update(Transaction transaction) {
    var financial = financialUseCase.findById(transaction.getFinancial().getId());
    validateUserPermission(financial);

    return transactionRepository.save(transaction);
  }

  public void deleteById(Long id) {
    var transaction = findById(id);
    var financial = transaction.getFinancial();
    validateUserPermission(financial);

    transactionRepository.deleteById(id);
  }

  public List<Transaction> findByPeriod(Long financialId, LocalDate startDate,
      LocalDate endDate) {
    var financial = financialUseCase.findById(financialId);
    validateUserPermission(financial);
    return transactionRepository.findByFinancialAndDateBetween(financialId, startDate, endDate);
  }

  public Page<Transaction> findByPeriodPaged(Long financialId, LocalDate startDate,
      LocalDate endDate, Pageable pageable) {
    var financial = financialUseCase.findById(financialId);
    validateUserPermission(financial);
    return transactionRepository.findByFinancialAndDateBetween(financialId, startDate, endDate,
        pageable);
  }

  public BigDecimal calculateBalance(Long financialId) {
    var financial = financialUseCase.findById(financialId);
    validateUserPermission(financial);
    return transactionRepository.calculateBalanceByFinancialId(financial.getId());
  }

  public PeriodSummary calculateFinancialPeriodSummary(Long financialId, LocalDate startDate,
      LocalDate endDate) {
    var financial = financialUseCase.findById(financialId);
    validateUserPermission(financial);
    var periodSummary = transactionRepository.calculatePeriodSummaryByFinancialId(financial.getId(),
        startDate,
        endDate);

    periodSummary.setTotalBalance(calculateBalance(financialId));
    return periodSummary;
  }

  public List<PeriodSummary> calculateFinancialPeriodSummaryReport(Long financialId, Integer month,
      Integer year) {
    var financial = financialUseCase.findById(financialId);
    validateUserPermission(financial);

    var summariesReport = new ArrayList<PeriodSummary>();

    for (int i = 0; i < 5; i++) {
      LocalDate date = LocalDate.of(year, month, 1);
      date = date.minusMonths(i);

      LocalDate startDate = date.withDayOfMonth(1);
      LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());

      PeriodSummary summary = transactionRepository.calculatePeriodSummaryByFinancialId(
          financialId, startDate, endDate);

      summary.setName(date.getMonth().getDisplayName(SHORT, Locale.of("pt", "BR"))
                      + "/" + date.getYear());
      summary.setTotalBalance(calculateBalance(financialId));

      summariesReport.add(summary);
    }

    return summariesReport;
  }

  public List<CategorySummary> calculateFinancialPeriodCategoryExpensesReport(Long financialId,
      Integer month,
      Integer year) {
    var financial = financialUseCase.findById(financialId);
    LocalDate date = LocalDate.of(year, month, 1);

    LocalDate startDate = date.withDayOfMonth(1);
    LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
    validateUserPermission(financial);
    return transactionRepository.calculateExpensesByCategory(financialId, startDate, endDate);
  }

  private void validateUserPermission(Financial financial) {
    var currentUser = getCurrentUser();

    if (Objects.isNull(currentUser)) {
      throw new SystemErrorException(SystemErrorMessage.ERR007.getFormattedMessage());
    }

    boolean isOwner = financial.getOwner().getId().equals(currentUser.getUser().getId());
    boolean isShared = financial.getSharedWith().stream()
        .anyMatch(user -> user.getId().equals(currentUser.getUser().getId()));

    if (!isOwner && !isShared) {
      throw new SystemErrorException(SystemErrorMessage.ERR006.getFormattedMessage());
    }
  }

  private void validateTransactionDetails(Transaction transaction) {
    var details = transaction.getDetails();
    if (transaction.getType() == TransactionType.INCOME &&
        (Objects.isNull(details) || Objects.isNull(
            details.getIncomeSource()) || details.getIncomeSource().isBlank())) {
      throw new SystemErrorException(SystemErrorMessage.ERR010.getFormattedMessage());
    } else if (transaction.getType() == TransactionType.EXPENSE &&
               (Objects.isNull(details) || Objects.isNull(details.getPaymentMethod()))) {
      throw new SystemErrorException(SystemErrorMessage.ERR011.getFormattedMessage());
    }
  }

}
