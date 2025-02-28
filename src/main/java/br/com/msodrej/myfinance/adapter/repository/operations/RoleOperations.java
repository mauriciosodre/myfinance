package br.com.msodrej.myfinance.adapter.repository.operations;

import br.com.msodrej.myfinance.adapter.repository.mapper.RoleEntityMapper;
import br.com.msodrej.myfinance.adapter.repository.repositories.RoleRepository;
import br.com.msodrej.myfinance.domain.exceptions.DatabaseErrorException;
import br.com.msodrej.myfinance.domain.model.Role;
import br.com.msodrej.myfinance.port.repository.RoleRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleOperations implements RoleRepositoryPort {

  private final RoleRepository roleRepository;
  private final RoleEntityMapper mapper;

  @Override
  public Role save(Role role) {
    try {
      return mapper.toModel(roleRepository.save(mapper.toEntity(role)));
    } catch (Exception e) {
      throw new DatabaseErrorException("Error saving role, cause: " + e.getMessage());
    }
  }

  @Override
  public Optional<Role> findById(Long id) {
    try {
      return roleRepository.findById(id).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error finding role by id, cause: " + e.getMessage());
    }
  }

  @Override
  public Page<Role> findAll(Pageable pageable) {
    try {
      return roleRepository.findAll(pageable).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error finding roles, cause: " + e.getMessage());
    }
  }

  @Override
  public void deleteById(Long id) {
    try {
      roleRepository.deleteById(id);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error deleting role by id, cause: " + e.getMessage());
    }
  }
}
