package br.com.msodrej.myfinance.adapter.repository.operations;

import br.com.msodrej.myfinance.adapter.repository.mapper.CategoryEntityMapper;
import br.com.msodrej.myfinance.adapter.repository.repositories.CategoryRepository;
import br.com.msodrej.myfinance.domain.exceptions.DatabaseErrorException;
import br.com.msodrej.myfinance.domain.model.Category;
import br.com.msodrej.myfinance.port.repository.CategoryRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryOperations implements CategoryRepositoryPort {

  private final CategoryRepository categoryRepository;
  private final CategoryEntityMapper mapper;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Category save(Category category) {
    try {
      return mapper.toModel(categoryRepository.save(mapper.toEntity(category)));
    } catch (Exception e) {
      throw new DatabaseErrorException("Error saving category, cause: " + e.getMessage());
    }
  }

  @Override
  public Optional<Category> findById(Long id) {
    try {
      return categoryRepository.findById(id).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error finding category by Id, cause: " + e.getMessage());
    }
  }

  @Override
  public Optional<Category> findByName(String name) {
    return categoryRepository.findByNameIgnoreCase(name).map(mapper::toModel);
  }

  @Override
  public Page<Category> findAll(Category category, Pageable pageable) {
    try {
      return categoryRepository.findAll(pageable).map(mapper::toModel);
    } catch (Exception e) {
      throw new DatabaseErrorException("Error finding categorys, cause: " + e.getMessage());
    }
  }

  @Override
  public List<Category> listAll() {
    try {
      return categoryRepository.findAll().stream().map(mapper::toModel).toList();
    } catch (Exception e) {
      throw new DatabaseErrorException("Error finding categorys, cause: " + e.getMessage());
    }
  }

  @Override
  public void deleteById(Long id) {
    try {
      categoryRepository.deleteById(id);
    } catch (Exception e) {
      throw new DatabaseErrorException(
          "Error deleting category by Id, cause: " + e.getMessage());
    }
  }
}
