package br.com.msodrej.myfinance.domain.usecase;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR005;

import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.model.Category;
import br.com.msodrej.myfinance.port.repository.CategoryRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryUseCase {

  private final CategoryRepositoryPort categoryRepository;

  public Category save(Category category) {
    return categoryRepository.save(category);
  }

  public Category findById(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new SystemErrorException(
        ERR005.getFormattedMessage(id)));
  }

  public Page<Category> findAll(Category category, Pageable pageable) {
    return categoryRepository.findAll(category, pageable);
  }

  public List<Category> listAll() {
    return categoryRepository.listAll();
  }

  public void deleteById(Long id) {
    var category = findById(id);
    try {
      categoryRepository.deleteById(category.getId());
    } catch (Exception e) {
      category.setEnabled(false);
      category.setDeleted(true);
      save(category);
    }
  }

}
