package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR006;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.msodrej.myfinance.adapter.config.security.UserPrincipal;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Financial;
import br.com.msodrej.myfinance.domain.model.Transaction;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.domain.utils.SecurityUtils;
import br.com.msodrej.myfinance.port.repository.TransactionRepositoryPort;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TransactionUseCaseTest {

  @Mock
  private TransactionRepositoryPort transactionRepository;
  @Mock
  private FinancialUseCase financialUseCase;
  @InjectMocks
  private TransactionUseCase transactionUseCase;

  private User owner;
  private User sharedUser;
  private User unauthorizedUser;
  private Financial financial;

  @BeforeEach
  void setUp() {
    owner = User.builder().id(UUID.randomUUID()).build();
    sharedUser = User.builder().id(UUID.randomUUID()).build();
    unauthorizedUser = User.builder().id(UUID.randomUUID()).build();

    financial = Financial.builder()
        .id(1L)
        .owner(owner)
        .sharedWith(new HashSet<>())
        .build();
    financial.getSharedWith().add(sharedUser);
  }

  @Test
  void save_ShouldThrowException_WhenUnauthorizedUserTries() {
    var transaction = Transaction.builder().financial(financial).build();
    var unauthorizedPrincipal = new UserPrincipal(unauthorizedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(unauthorizedPrincipal);

      when(financialUseCase.findById(1L)).thenReturn(financial);

      assertThatThrownBy(() -> transactionUseCase.save(transaction))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR006.getFormattedMessage());
    }
  }


  @Test
  void save_ShouldSucceed_WhenOwnerCreatesTransaction() {
    var transaction = Transaction.builder().financial(financial).build();
    var ownerPrincipal = new UserPrincipal(owner);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(ownerPrincipal);

      when(financialUseCase.findById(1L)).thenReturn(financial);
      when(transactionRepository.save(transaction)).thenReturn(transaction);

      transactionUseCase.save(transaction);

      verify(transactionRepository, times(1)).save(transaction);
    }
  }

  @Test
  void save_ShouldSucceed_WhenSharedUserCreatesTransaction() {
    var transaction = Transaction.builder().financial(financial).build();
    var sharedPrincipal = new UserPrincipal(sharedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(sharedPrincipal);

      when(financialUseCase.findById(1L)).thenReturn(financial);
      when(transactionRepository.save(transaction)).thenReturn(transaction);

      transactionUseCase.save(transaction);

      verify(transactionRepository, times(1)).save(transaction);
    }
  }

  @Test
  void update_ShouldSucceed_WhenOwnerUpdatesTransaction() {
    var transaction = Transaction.builder().id(1L).financial(financial).build();
    var ownerPrincipal = new UserPrincipal(owner);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(ownerPrincipal);

      when(financialUseCase.findById(1L)).thenReturn(financial);
      when(transactionRepository.save(transaction)).thenReturn(transaction);

      Transaction result = transactionUseCase.update(transaction);

      verify(transactionRepository, times(1)).save(transaction);
      assertThat(result).isEqualTo(transaction);
    }
  }

  @Test
  void update_ShouldThrowException_WhenUnauthorizedUserTries() {
    var transaction = Transaction.builder().id(1L).financial(financial).build();
    var unauthorizedPrincipal = new UserPrincipal(unauthorizedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(unauthorizedPrincipal);

      when(financialUseCase.findById(1L)).thenReturn(financial);

      assertThatThrownBy(() -> transactionUseCase.update(transaction))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR006.getFormattedMessage());
    }
  }

  @Test
  void deleteById_ShouldSucceed_WhenSharedUserDeletesTransaction() {
    var transaction = Transaction.builder().id(1L).financial(financial).build();
    var sharedPrincipal = new UserPrincipal(sharedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(sharedPrincipal);

      when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

      transactionUseCase.deleteById(1L);

      verify(transactionRepository, times(1)).deleteById(1L);
    }
  }

  @Test
  void deleteById_ShouldThrowException_WhenUnauthorizedUserTries() {
    var transaction = Transaction.builder().id(1L).financial(financial).build();
    var unauthorizedPrincipal = new UserPrincipal(unauthorizedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(unauthorizedPrincipal);

      when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

      assertThatThrownBy(() -> transactionUseCase.deleteById(1L))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR006.getFormattedMessage());
    }
  }

}