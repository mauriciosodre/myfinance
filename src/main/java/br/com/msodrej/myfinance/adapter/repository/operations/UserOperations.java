package br.com.msodrej.myfinance.adapter.repository.operations;

import br.com.msodrej.myfinance.adapter.repository.mapper.UserEntityMapper;
import br.com.msodrej.myfinance.adapter.repository.repositories.UserRepository;
import br.com.msodrej.myfinance.adapter.repository.specifications.UserSpecification;
import br.com.msodrej.myfinance.domain.exceptions.DatabaseErrorException;
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
    try {
      return mapper.toModel(repository.save(mapper.toEntity(user)));
    } catch (Exception e) {
      throw new DatabaseErrorException("Error saving user, cause: " + e.getMessage());
    }
  }

  @Override
  public Optional<User> findById(UUID id) {
    try {
      return repository.findById(id).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("User not found, cause: " + e.getMessage());
    }
  }

  @Override
  public Optional<User> findByUsername(String username) {
    try {
      return repository.findByUsername(username).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("User not found, cause: " + e.getMessage());
    }
  }

  @Override
  public Page<User> findAll(User user, Pageable pageable) {
    try {
      var specification = UserSpecification.filter(user);
      return repository.findAll(specification, pageable).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error finding users, cause: " + e.getMessage());
    }
  }

  @Override
  public void deleteById(UUID id) {
    try {
      repository.deleteById(id);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error deleting user, cause: " + e.getMessage());
    }
  }

  @Override
  public void activate(UUID id, boolean active) {
    try {
      repository.activateById(id, active);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error activating user, cause: " + e.getMessage());
    }
  }

  @Override
  public boolean existsById(UUID id) {
    try {
      return repository.existsById(id);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error checking if user exists, cause: " + e.getMessage());
    }
  }
}
