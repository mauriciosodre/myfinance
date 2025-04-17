package br.com.msodrej.myfinance.adapter.repository.repositories;

import br.com.msodrej.myfinance.adapter.repository.entity.CategoryEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

  Optional<CategoryEntity> findByNameIgnoreCase(String name);

}
