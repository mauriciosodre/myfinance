package br.com.msodrej.myfinance.adapter.repository.operations;

import br.com.msodrej.myfinance.adapter.repository.mapper.UserEntityMapper;
import br.com.msodrej.myfinance.adapter.repository.repositories.UserRepository;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.port.repository.UserRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserOperations implements UserRepositoryPort {

  private final UserRepository repository;
  private final UserEntityMapper mapper;

  @Override
  public User save(User user) {
    return mapper.toModel(repository.save(mapper.toEntity(user)));
  }

  @Override
  public Optional<User> findById(UUID id) {
    return repository.findById(id).map(mapper::toModel);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return repository.findByUsername(username).map(mapper::toModel);
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toModel);
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id);
  }

  @Override
  public void activate(UUID id, boolean active) {
    repository.activateById(id, active);
  }

  @Override
  public boolean existsById(UUID id) {
    return repository.existsById(id);
  }
}
