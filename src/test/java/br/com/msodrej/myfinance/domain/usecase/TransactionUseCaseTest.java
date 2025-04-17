package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR005;
import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR006;
import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR007;
import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR010;
import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR011;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.msodrej.myfinance.adapter.config.security.UserPrincipal;
import br.com.msodrej.myfinance.domain.enums.TransactionType;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Category;
import br.com.msodrej.myfinance.domain.model.Financial;
import br.com.msodrej.myfinance.domain.model.Transaction;
import br.com.msodrej.myfinance.domain.model.TransactionDetails;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.domain.utils.SecurityUtils;
import br.com.msodrej.myfinance.port.repository.TransactionRepositoryPort;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    // Configuração de usuários
    owner = User.builder().id(UUID.randomUUID()).email("owner@example.com").build();
    sharedUser = User.builder().id(UUID.randomUUID()).email("shared@example.com").build();
    unauthorizedUser = User.builder().id(UUID.randomUUID()).email("unauthorized@example.com")
        .build();

    // Configuração do controle financeiro
    financial = Financial
        .builder()
        .id(1L)
        .owner(owner)
        .name("Controle Financeiro")
        .description("Descrição do controle financeiro")
        .sharedWith(Set.of(sharedUser))
        .build();

    // Configuração da transação
    transaction = Transaction
        .builder()
        .id(1L)
        .financial(financial)
        .amount(new BigDecimal("100.00"))
        .category(Category.builder().id(1L).build())
        .build();
  }

  @Test
  void save_ShouldThrowException_WhenUnauthorizedUserTries() {
    var unauthorizedPrincipal = new UserPrincipal(unauthorizedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
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
    var ownerPrincipal = new UserPrincipal(owner);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
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
    var sharedPrincipal = new UserPrincipal(sharedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(sharedPrincipal);

      when(financialUseCase.findById(1L)).thenReturn(financial);
      when(transactionRepository.save(transaction)).thenReturn(transaction);

      transactionUseCase.save(transaction);

      verify(transactionRepository, times(1)).save(transaction);
    }
  }

  @Test
  void save_ShouldThrowException_WhenIncomeWithoutSource() {
    transaction.setType(TransactionType.INCOME);
    transaction.setDetails(new TransactionDetails(null, null, false, null));

    var sharedPrincipal = new UserPrincipal(sharedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(sharedPrincipal);
      when(financialUseCase.findById(1L)).thenReturn(financial);
      assertThatThrownBy(() -> transactionUseCase.save(transaction))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR010.getFormattedMessage());
    }
  }

  @Test
  void save_ShouldThrowException_WhenExpenseWithoutPaymentMethod() {
    transaction.setType(TransactionType.EXPENSE);
    transaction.setDetails(new TransactionDetails(null, null, false, null));

    var sharedPrincipal = new UserPrincipal(sharedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(sharedPrincipal);
      when(financialUseCase.findById(1L)).thenReturn(financial);
      assertThatThrownBy(() -> transactionUseCase.save(transaction))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR011.getFormattedMessage());
    }
  }

  @Test
  void findById_ShouldSucceed() {

    when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

    var result = transactionUseCase.findById(1L);

    assertThat(result).isEqualTo(transaction);
  }

  @Test
  void findById_ShouldThrownError_WhenTransactionNotExist() {

    when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> transactionUseCase.findById(1L))
        .isInstanceOf(SystemErrorException.class)
        .hasMessage(ERR005.getFormattedMessage(1L));
  }

  @Test
  void findAll_ShouldSucceed() {
    var pageable = mock(org.springframework.data.domain.Pageable.class);
    when(transactionRepository.findAll(transaction, pageable)).thenReturn(
        mock(org.springframework.data.domain.Page.class));

    var result = transactionUseCase.findAll(transaction, pageable);

    assertThat(result).isNotNull();
  }

  @Test
  void update_ShouldSucceed_WhenOwnerUpdatesTransaction() {
    var ownerPrincipal = new UserPrincipal(owner);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
        SecurityUtils.class)) {
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
    var unauthorizedPrincipal = new UserPrincipal(unauthorizedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(unauthorizedPrincipal);

      when(financialUseCase.findById(1L)).thenReturn(financial);

      assertThatThrownBy(() -> transactionUseCase.update(transaction))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR006.getFormattedMessage());
    }
  }

  @Test
  void deleteById_ShouldSucceed_WhenSharedUserDeletesTransaction() {
    var sharedPrincipal = new UserPrincipal(sharedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(sharedPrincipal);

      when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

      transactionUseCase.deleteById(1L);

      verify(transactionRepository, times(1)).deleteById(1L);
    }
  }

  @Test
  void deleteById_ShouldThrowException_WhenUnauthorizedUserTries() {
    var unauthorizedPrincipal = new UserPrincipal(unauthorizedUser);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(unauthorizedPrincipal);

      when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

      assertThatThrownBy(() -> transactionUseCase.deleteById(1L))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR006.getFormattedMessage());
    }
  }

  @Test
  void shouldThrowExceptionWhenUserIsNotAuthenticated() {
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(null);
      when(financialUseCase.findById(1L)).thenReturn(financial);

      assertThatThrownBy(() -> transactionUseCase.save(transaction))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR007.getFormattedMessage());
      verify(transactionRepository, never()).save(transaction);
    }
  }

  @Test
  void shouldThrowExceptionWhenFinancialNotExist() {
    var currentUser = new UserPrincipal(owner);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(currentUser);
      when(financialUseCase.findById(1L)).thenThrow(
          new SystemErrorException(ERR007.getFormattedMessage(1L)));

      assertThatThrownBy(() -> transactionUseCase.save(transaction))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR007.getFormattedMessage(1L));
      verify(transactionRepository, never()).save(transaction);
    }
  }

}