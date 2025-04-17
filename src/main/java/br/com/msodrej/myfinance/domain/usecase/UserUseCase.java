package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR001;
import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR002;

import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.port.repository.UserRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserUseCase {

  private final UserRepositoryPort repositoryPort;

  public User save(User user) {
    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    return repositoryPort.save(user);
  }

  public User update(User user) {
    checkIfUserExists(user.getId());
    return repositoryPort.save(user);
  }

  public User findById(UUID id) {
    return repositoryPort.findById(id)
        .orElseThrow(() -> new SystemErrorException(ERR001.getFormattedMessage(id)));
  }

  public User findByUsername(String username) {
    return repositoryPort.findByUsername(username)
        .orElseThrow(() -> new SystemErrorException(ERR002.getFormattedMessage(username)));
  }

  public void deleteById(UUID id) {
    checkIfUserExists(id);
    repositoryPort.deleteById(id);
  }

  public void activate(UUID id) {
    var user = findById(id);
    repositoryPort.activate(id, !user.getActive());
  }

  public boolean existsById(UUID id) {
    return repositoryPort.existsById(id);
  }

  public Page<User> findAll(User user, Pageable pageable) {
    return repositoryPort.findAll(user, pageable);
  }

  private void checkIfUserExists(UUID id) {
    if (!existsById(id)) {
      throw new SystemErrorException(ERR001.getFormattedMessage(id));
    }
  }

}
