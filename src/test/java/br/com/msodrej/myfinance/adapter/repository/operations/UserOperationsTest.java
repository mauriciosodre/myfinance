package br.com.msodrej.myfinance.adapter.repository.operations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.msodrej.myfinance.adapter.repository.entity.UserEntity;
import br.com.msodrej.myfinance.adapter.repository.mapper.UserEntityMapper;
import br.com.msodrej.myfinance.adapter.repository.repositories.UserRepository;
import br.com.msodrej.myfinance.domain.model.User;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserOperationsTest {

  @InjectMocks
  private UserOperations userOperations;
  @Mock
  private UserRepository repository;
  @Mock
  private UserEntityMapper mapper;
  
  private static final UUID ID = UUID.randomUUID();

  @Test
  void save() {
    var user = createUser();
    var entity = createUserEntity();
    var savedUser = createUser();
    savedUser.setActive(true);
    when(mapper.toEntity(user)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(mapper.toModel(entity)).thenReturn(savedUser);

    var result = userOperations.save(user);
    Assertions.assertThat(result).isNotNull().isEqualTo(savedUser);
  }

  @Test
  void findById() {
    var user = createUser();
    var entity = createUserEntity();
    when(repository.findById(user.getId())).thenReturn(Optional.of(entity));
    when(mapper.toModel(entity)).thenReturn(user);

    var result = userOperations.findById(user.getId());
    Assertions.assertThat(result).isNotNull().isEqualTo(Optional.of(user));
  }

  @Test
  void findAll() {
    var usersPage = new PageImpl<>(List.of(createUserEntity()));
    var user = createUser();
    when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(usersPage);
    when(mapper.toModel(any(UserEntity.class))).thenReturn(user);

    var result = userOperations.findAll(User.builder().build(), Pageable.unpaged());
    Assertions.assertThat(result).isNotNull().isNotEmpty().hasSize(1).contains(user);
  }

  @Test
  void deleteById() {
    when(repository.existsById(any())).thenReturn(true);
    userOperations.deleteById(ID);
    verify(repository, times(1)).deleteById(ID);
  }

  @Test
  void activate() {
    when(repository.existsById(any())).thenReturn(true);
    userOperations.activate(ID, true);
    verify(repository, times(1)).activateById(ID, true);
  }

  @Test
  void existsById() {
    when(repository.existsById(any())).thenReturn(true);
    var result = userOperations.existsById(ID);
    Assertions.assertThat(result).isTrue();
  }

  private User createUser() {
    return User
        .builder()
        .id(ID)
        .name("name")
        .email("email")
        .phone("phone")
        .build();
  }

  private UserEntity createUserEntity() {
    return UserEntity
        .builder()
        .id(ID)
        .name("name")
        .email("email")
        .phone("phone")
        .active(true)
        .build();
  }
}