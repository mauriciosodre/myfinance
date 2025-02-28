package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR001;
import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR002;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.port.repository.UserRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserUseCaseTest {

  @InjectMocks
  private UserUseCase userUseCase;
  @Mock
  private UserRepositoryPort userRepositoryPort;
  
  private static final UUID ID = UUID.randomUUID();

  @Test
  void save() {
    var user = createUser();
    user.setPassword(new BCryptPasswordEncoder().encode("password"));
    user.setActive(true);
    when(userRepositoryPort.save(any())).thenReturn(user);

    var payload = createUser();
    payload.setPassword("password");
    var result = userUseCase.save(payload);

    assertThat(result).isNotNull();
    assertThat(new BCryptPasswordEncoder().matches("password", result.getPassword())).isTrue();
    assertThat(result.getActive()).isTrue();
  }

  @Test
  void update() {
    var user = createUser();
    when(userRepositoryPort.save(any())).thenReturn(user);
    when(userRepositoryPort.existsById(any())).thenReturn(true);
    var result = userUseCase.update(user);
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(user);
  }

  @Test
  void shouldThrowExceptionWhenUpdateUserThatNotExists() {
    var user = createUser();
    when(userRepositoryPort.existsById(any())).thenReturn(false);
    Assertions.assertThatThrownBy(() -> userUseCase.update(user))
        .isInstanceOf(SystemErrorException.class)
        .hasMessageContaining(ERR001.getFormattedMessage(user.getId()));
  }

  @Test
  void findById() {
    var user = createUser();
    when(userRepositoryPort.findById(any())).thenReturn(Optional.of(user));
    var result = userUseCase.findById(user.getId());
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(user);
  }

  @Test
  void shouldThrowExceptionWhenFindUserThatNotExists() {
    when(userRepositoryPort.findById(any())).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> userUseCase.findById(ID))
        .isInstanceOf(SystemErrorException.class)
        .hasMessageContaining(ERR001.getFormattedMessage(ID));
  }

  @Test
  void deleteById() {
    when(userRepositoryPort.existsById(any())).thenReturn(true);
    userUseCase.deleteById(ID);
    verify(userRepositoryPort, times(1)).deleteById(any());
  }

  @Test
  void shouldThrowExceptionWhenDeleteUserThatNotExists() {
    when(userRepositoryPort.existsById(any())).thenReturn(false);
    Assertions.assertThatThrownBy(() -> userUseCase.deleteById(ID))
        .isInstanceOf(SystemErrorException.class)
        .hasMessageContaining(ERR001.getFormattedMessage(ID));
  }

  @Test
  void activate() {
    var user = createUser();
    user.setActive(false);
    when(userRepositoryPort.findById(any())).thenReturn(Optional.of(user));
    userUseCase.activate(ID);
    verify(userRepositoryPort, times(1)).activate(any(), eq(true));
  }

  @Test
  void deactivate() {
    var user = createUser();
    user.setActive(true);
    when(userRepositoryPort.findById(any())).thenReturn(Optional.of(user));
    userUseCase.activate(ID);
    verify(userRepositoryPort, times(1)).activate(any(), eq(false));
  }

  @Test
  void shouldThrowExceptionWhenActivateUserThatNotExists() {
    when(userRepositoryPort.findById(any())).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> userUseCase.activate(ID))
        .isInstanceOf(SystemErrorException.class)
        .hasMessageContaining(ERR001.getFormattedMessage(ID));
  }

  @Test
  void existsById() {
    when(userRepositoryPort.existsById(any())).thenReturn(true);
    var result = userUseCase.existsById(ID);
    assertThat(result).isTrue();
  }

  @Test
  void findAll() {
    var UsersPage = new PageImpl<>(List.of(createUser()));
    when(userRepositoryPort.findAll(any(Pageable.class))).thenReturn(UsersPage);
    var result = userUseCase.findAll(Pageable.unpaged());
    assertThat(result).isNotNull().isNotEmpty().size().isEqualTo(1);
  }

  @Test
  void findByUsername() {
    var user = createUser();
    when(userRepositoryPort.findByUsername(any())).thenReturn(Optional.of(user));
    var result = userUseCase.findByUsername("username");
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(user);
  }

  @Test
  void shouldThrowExceptionWhenFindUserByUsernameThatNotExists() {
    when(userRepositoryPort.findByUsername(any())).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> userUseCase.findByUsername("username"))
        .isInstanceOf(SystemErrorException.class)
        .hasMessageContaining(ERR002.getFormattedMessage("username"));
  }

  private User createUser() {
    return User.builder()
        .id(ID)
        .name("Test")
        .email("email")
        .phone("phone")
        .build();
  }
}