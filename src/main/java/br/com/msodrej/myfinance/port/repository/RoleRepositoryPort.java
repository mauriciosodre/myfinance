package br.com.msodrej.myfinance.port.repository;

import br.com.msodrej.myfinance.domain.model.Role;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleRepositoryPort {

  Role save(Role role);

  Optional<Role> findById(Long id);

  Page<Role> findAll(Pageable pageable);

  void deleteById(Long id);

}
