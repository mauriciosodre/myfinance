package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR008;
import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR009;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.msodrej.myfinance.adapter.config.security.UserPrincipal;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Financial;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.domain.utils.SecurityUtils;
import br.com.msodrej.myfinance.port.repository.FinancialRepositoryPort;
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
class FinancialUseCaseTest {

  @Mock
  private FinancialRepositoryPort financialRepository;
  @Mock
  private UserUseCase userUseCase;
  @InjectMocks
  private FinancialUseCase financialUseCase;

  private User owner;
  private User sharedUser;
  private Financial financial;

  @BeforeEach
  void setUp() {
    owner = User.builder().id(UUID.randomUUID()).build();
    sharedUser = User.builder().id(UUID.randomUUID()).build();
    financial = Financial.builder()
        .id(1L)
        .owner(owner)
        .sharedWith(new HashSet<>())
        .build();
  }

  @Test
  void addUserToSharing_ShouldAddUser_WhenOwnerRequests() {
    financial.getSharedWith().add(sharedUser);
    var userPrincipal = new UserPrincipal(owner);
    var newSharedUser = User.builder().id(UUID.randomUUID()).build();
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(userPrincipal);

      when(financialRepository.findById(1L)).thenReturn(Optional.of(financial));
      when(userUseCase.findById(newSharedUser.getId())).thenReturn(newSharedUser);

      financialUseCase.addUserToSharing(1L, newSharedUser.getId());

      verify(financialRepository, times(1)).save(financial);
    }
  }

  @Test
  void addUserToSharing_ShouldThrowException_WhenNotOwner() {
    var nonOwner = User.builder().id(UUID.randomUUID()).build();
    var userPrincipal = new UserPrincipal(nonOwner);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(userPrincipal);

      when(financialRepository.findById(1L)).thenReturn(Optional.of(financial));

      var sharedUserId = sharedUser.getId();

      assertThatThrownBy(() -> financialUseCase.addUserToSharing(1L, sharedUserId))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR008.getFormattedMessage());
    }
  }

  @Test
  void addUserToSharing_ShouldThrowException_WhenUserIsAlreadyShared() {
    var userPrincipal = new UserPrincipal(owner);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(userPrincipal);

      financial.getSharedWith().add(sharedUser);

      when(financialRepository.findById(1L)).thenReturn(Optional.of(financial));
      when(userUseCase.findById(sharedUser.getId())).thenReturn(sharedUser);

      var sharedUserId = sharedUser.getId();

      assertThatThrownBy(() -> financialUseCase.addUserToSharing(1L, sharedUserId))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR009.getFormattedMessage());
    }
  }

  @Test
  void removeUserFromSharing_ShouldRemoveUser_WhenOwnerRequests() {
    financial.getSharedWith().add(sharedUser);
    var userPrincipal = new UserPrincipal(owner);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(userPrincipal);

      when(financialRepository.findById(1L)).thenReturn(Optional.of(financial));
      when(userUseCase.findById(sharedUser.getId())).thenReturn(sharedUser);

      financialUseCase.removeUserFromSharing(1L, sharedUser.getId());

      verify(financialRepository, times(1)).save(financial);
    }
  }

  @Test
  void removeUserFromSharing_ShouldThrowException_WhenNotOwner() {
    var nonOwner = User.builder().id(UUID.randomUUID()).build();
    var userPrincipal = new UserPrincipal(nonOwner);
    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(
        SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(userPrincipal);

      when(financialRepository.findById(1L)).thenReturn(Optional.of(financial));

      var sharedUserId = sharedUser.getId();

      assertThatThrownBy(() -> financialUseCase.removeUserFromSharing(1L, sharedUserId))
          .isInstanceOf(SystemErrorException.class)
          .hasMessage(ERR008.getFormattedMessage());
    }
  }

}