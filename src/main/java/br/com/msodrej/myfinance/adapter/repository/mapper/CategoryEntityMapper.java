package br.com.msodrej.myfinance.adapter.repository.mapper;

import br.com.msodrej.myfinance.adapter.repository.entity.CategoryEntity;
import br.com.msodrej.myfinance.domain.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {

  Category toModel(CategoryEntity categoryEntity);

  CategoryEntity toEntity(Category category);

}
