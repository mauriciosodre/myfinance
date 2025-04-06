package br.com.msodrej.myfinance.domain.utils;

import br.com.msodrej.myfinance.adapter.config.security.UserPrincipal;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SecurityUtilsTest {

  @Test
  void getCurrentUser_ShouldReturnUserPrincipal_WhenAuthenticated() {
    var userPrincipal = new UserPrincipal(User.builder().id(UUID.randomUUID()).build());
    var authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userPrincipal);
    var securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);

    try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
      mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      var result = SecurityUtils.getCurrentUser();

      assertThat(result).isEqualTo(userPrincipal);
    }
  }

  @Test
  void getCurrentUser_ShouldThrowException_WhenNotAuthenticated() {
    var securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(null);

    try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
      mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      assertThatThrownBy(SecurityUtils::getCurrentUser)
          .isInstanceOf(SystemErrorException.class)
          .hasMessage("User not authenticated");
    }
  }
}