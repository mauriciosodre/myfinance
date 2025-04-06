package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR004;
import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR008;
import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR009;

import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Financial;
import br.com.msodrej.myfinance.domain.model.User;
import br.com.msodrej.myfinance.domain.utils.SecurityUtils;
import br.com.msodrej.myfinance.port.repository.FinancialRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialUseCase {

  private final FinancialRepositoryPort financialRepository;
  private final UserUseCase userUseCase;

  public Financial save(Financial financial) {
    return financialRepository.save(financial);
  }

  public Financial findById(Long id) {
    return financialRepository.findById(id).orElseThrow(() -> new SystemErrorException(
        ERR004.getFormattedMessage(id)));
  }

  public Page<Financial> findAll(Financial financial, Pageable pageable) {
    return financialRepository.findAll(financial, pageable);
  }

  public void delete(Long id) {
    var financial = findById(id);
    financialRepository.deleteById(financial.getId());
  }

  public void addUserToSharing(Long financialId, UUID userId) {

    var financial = findById(financialId);

    validateUserOwnership(financial);

    var userToAdd = userUseCase.findById(userId);

    if (financial.getSharedWith().contains(userToAdd)) {
      throw new SystemErrorException(ERR009.getFormattedMessage());
    }

    financial.getSharedWith().add(userToAdd);
    financialRepository.save(financial);
  }

  public void removeUserFromSharing(Long financialId, UUID userId) {
    var financial = findById(financialId);

    validateUserOwnership(financial);

    financial.getSharedWith().removeIf(u -> u.getId().equals(userId));

    financialRepository.save(financial);
  }

  private void validateUserOwnership(Financial financial) {
    var currentUserPrincipal = SecurityUtils.getCurrentUser();
    User currentUser = currentUserPrincipal.getUser();

    if (!financial.getOwner().getId().equals(currentUser.getId())) {
      throw new SystemErrorException(ERR008.getFormattedMessage());
    }
  }

}
