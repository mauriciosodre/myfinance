package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR005;
import static br.com.msodrej.myfinance.domain.utils.SecurityUtils.getCurrentUser;

import br.com.msodrej.myfinance.domain.enums.SystemErrorMessage;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Financial;
import br.com.msodrej.myfinance.domain.model.Transaction;
import br.com.msodrej.myfinance.port.repository.TransactionRepositoryPort;
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

    return transactionRepository.save(transaction);
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

}
