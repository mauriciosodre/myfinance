package br.com.msodrej.myfinance.domain.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.msodrej.myfinance.adapter.config.security.AuthenticationService;
import br.com.msodrej.myfinance.adapter.config.security.UserPrincipal;
import br.com.msodrej.myfinance.domain.enums.SystemErrorMessage;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Financial;
import br.com.msodrej.myfinance.domain.model.Transaction;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.port.repository.TransactionRepositoryPort;
import java.util.HashSet;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TransactionUseCaseTest {

  @Mock
  private TransactionRepositoryPort transactionRepository;
  @Mock
  private FinancialUseCase financialUseCase;
  @Mock
  private AuthenticationService authenticationService;
  @InjectMocks
  private TransactionUseCase transactionUseCase;

  private static final UUID USER_ID = UUID.randomUUID();

  @Test
  void save_UnauthorizedUser_ThrowsError() {
    var financial = getFinancial();
    financial.setOwner(User.builder().id(UUID.randomUUID()).build());
    when(financialUseCase.findById(any())).thenReturn(financial);

    when(authenticationService.getAuthenticatedUser()).thenReturn(new UserPrincipal(getUser()));

    var transaction = getNewTransaction();

    assertThrows(SystemErrorException.class, () -> transactionUseCase.save(transaction),
        SystemErrorMessage.ERR006.getFormattedMessage());
  }

  @Test
  void update_UnauthorizedUser_ThrowsError() {
    var transaction = getNewTransaction();
    transaction.setId(1L);
    var financial = getFinancial();
    financial.setOwner(User.builder().id(UUID.randomUUID()).build());
    when(financialUseCase.findById(any())).thenReturn(financial);

    when(authenticationService.getAuthenticatedUser()).thenReturn(
        new UserPrincipal(User.builder().build()));

    assertThrows(SystemErrorException.class, () -> transactionUseCase.update(transaction),
        SystemErrorMessage.ERR006.getFormattedMessage());
  }

  @Test
  void deleteById_UnauthorizedUser_ThrowsError() {
    Transaction transaction = new Transaction();
    Financial financial = new Financial();
    financial.setOwner(User.builder().build());
    transaction.setFinancial(financial);

    when(authenticationService.getAuthenticatedUser()).thenReturn(
        new UserPrincipal(User.builder().build()));

    assertThrows(SystemErrorException.class, () -> transactionUseCase.deleteById(1L));
  }

  private Transaction getNewTransaction() {
    return Transaction
        .builder()
        .financial(getFinancial())
        .build();
  }

  private Financial getFinancial() {
    return Financial
        .builder()
        .name("Financial Name")
        .id(1L)
        .owner(getUser())
        .sharedWith(new HashSet<>())
        .build();
  }

  private User getUser() {
    return User.builder().id(USER_ID).build();
  }

}