package br.com.msodrej.myfinance.port.repository;

import br.com.msodrej.myfinance.domain.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryPort {

  User save(User user);

  Optional<User> findById(UUID id);

  Optional<User> findByUsername(String username);

  Page<User> findAll(User user, Pageable pageable);

  void deleteById(UUID id);

  void activate(UUID id, boolean active);

  boolean existsById(UUID id);

}
