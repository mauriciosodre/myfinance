package br.com.msodrej.myfinance.adapter.config.security;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.msodrej.myfinance.domain.enums.SystemErrorMessage;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.domain.usecase.UserUseCase;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {

  @InjectMocks
  private AuthenticationService authenticationService;

  @Mock
  private UserUseCase userUseCase;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationManager authenticationManager;

  private UserPrincipal userPrincipal;

  @BeforeEach
  void setUp() {
    User user = User.builder()
        .id(UUID.randomUUID())
        .username("testuser")
        .password("$2a$10$examplehash")
        .build();
    userPrincipal = new UserPrincipal(user);
    var authentication = mock(org.springframework.security.core.Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userPrincipal);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @Test
  void updatePassword_ShouldSucceed_WhenOldPasswordIsValid() {
    String oldPassword = "oldPass";
    String newPassword = "newPass";
    String encodedNewPassword = "$2a$10$newhash";

    when(passwordEncoder.matches(oldPassword, userPrincipal.getPassword())).thenReturn(true);
    when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
    when(userUseCase.save(any(User.class))).thenReturn(userPrincipal.getUser());

    authenticationService.updatePassword(oldPassword, newPassword);

    verify(userUseCase, times(1)).save(userPrincipal.getUser());
    verify(passwordEncoder, times(1)).encode(newPassword);
  }

  @Test
  void updatePassword_ShouldThrowException_WhenOldPasswordIsInvalid() {
    String oldPassword = "wrongPass";
    String newPassword = "newPass";

    when(passwordEncoder.matches(oldPassword, userPrincipal.getPassword())).thenReturn(false);

    assertThatThrownBy(() -> authenticationService.updatePassword(oldPassword, newPassword))
        .isInstanceOf(SystemErrorException.class)
        .hasMessage(SystemErrorMessage.ERR012.getFormattedMessage());

    verify(userUseCase, never()).save(any());
  }
}