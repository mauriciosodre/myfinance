package br.com.msodrej.myfinance.adapter.mapper;

import br.com.msodrej.myfinance.adapter.dto.category.CategoryPayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.category.CategoryResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.category.NewCategoryDTO;
import br.com.msodrej.myfinance.domain.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryDTOMapper {

  Category toModel(NewCategoryDTO dto);

  Category toModel(CategoryPayloadDTO dto);

  CategoryResponseDTO toDTO(Category category);
}