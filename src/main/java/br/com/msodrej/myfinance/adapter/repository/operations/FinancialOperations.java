package br.com.msodrej.myfinance.adapter.repository.operations;

import static br.com.msodrej.myfinance.adapter.repository.specifications.FinancialSpecification.hasName;
import static br.com.msodrej.myfinance.adapter.repository.specifications.FinancialSpecification.hasOwnerId;
import static br.com.msodrej.myfinance.adapter.repository.specifications.FinancialSpecification.isSharedWith;

import br.com.msodrej.myfinance.adapter.repository.mapper.FinancialEntityMapper;
import br.com.msodrej.myfinance.adapter.repository.repositories.FinancialRepository;
import br.com.msodrej.myfinance.domain.exceptions.DatabaseErrorException;
import br.com.msodrej.myfinance.domain.model.Financial;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.port.repository.FinancialRepositoryPort;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FinancialOperations implements FinancialRepositoryPort {

  private final FinancialRepository financialRepository;
  private final FinancialEntityMapper mapper;

  @Override
  public Financial save(Financial financial) {
    try {
      return mapper.toModel(financialRepository.save(mapper.toEntity(financial)));
    } catch (Exception e) {
      throw new DatabaseErrorException("Error saving financial, cause: " + e.getMessage());
    }
  }

  @Override
  public Optional<Financial> findById(Long id) {
    try {
      return financialRepository.findById(id).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error finding financial by Id, cause: " + e.getMessage());
    }
  }

  @Override
  public Page<Financial> findAll(Financial financial, Pageable pageable) {
    UUID sharedWithId = null;
    if (Objects.nonNull(financial.getSharedWith())) {
      sharedWithId = financial.getSharedWith()
          .stream()
          .map(User::getId)
          .findFirst().orElse(null);
    }

    var spec = Specification.where(hasName(financial.getName()))
        .and((hasOwnerId(financial.getOwner().getId())).or(isSharedWith(sharedWithId)));

    return financialRepository.findAll(spec, pageable).map(mapper::toModel);
  }

  @Override
  public void deleteById(Long id) {
    try {
      financialRepository.deleteById(id);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error deleting financial by Id, cause: " + e.getMessage());
    }
  }
}
