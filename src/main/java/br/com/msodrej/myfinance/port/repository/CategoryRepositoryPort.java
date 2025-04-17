package br.com.msodrej.myfinance.port.repository;

import br.com.msodrej.myfinance.domain.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepositoryPort {

  Category save(Category category);

  Optional<Category> findById(Long id);

  Optional<Category> findByName(String name);

  Page<Category> findAll(Category category, Pageable pageable);

  List<Category> listAll();

  void deleteById(Long id);

}
