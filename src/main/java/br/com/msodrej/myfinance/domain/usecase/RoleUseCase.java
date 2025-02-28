package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR003;

import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Role;
import br.com.msodrej.myfinance.port.repository.RoleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleUseCase {

  private final RoleRepositoryPort roleRepository;

  public Role save(Role role) {
    return roleRepository.save(role);
  }

  public Role findById(Long id) {
    return roleRepository.findById(id).orElseThrow(() -> new SystemErrorException(
        ERR003.getFormattedMessage(id)));
  }

  public Page<Role> findAll(Pageable pageable) {
    return roleRepository.findAll(pageable);
  }

  public void delete(Long id) {
    var role = findById(id);
    roleRepository.deleteById(role.getId());
  }


}
