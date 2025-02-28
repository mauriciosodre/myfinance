package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR004;

import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Financial;
import br.com.msodrej.myfinance.port.repository.FinancialRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialUseCase {

  private final FinancialRepositoryPort financialRepository;

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

}
