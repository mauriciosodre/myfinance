package br.com.msodrej.myfinance.adapter.controller;

import br.com.msodrej.myfinance.adapter.controller.exceptions.ErrorDetails;
import br.com.msodrej.myfinance.adapter.dto.category.CategoryPayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.category.CategoryResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.category.NewCategoryDTO;
import br.com.msodrej.myfinance.adapter.mapper.CategoryDTOMapper;
import br.com.msodrej.myfinance.domain.model.Category;
import br.com.msodrej.myfinance.domain.usecase.CategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categorys")
public class CategoryController {

  private final CategoryUseCase categoryUseCase;
  private final CategoryDTOMapper mapper;

  @Operation(summary = "Criar uma nova categoria")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Categoria criadad",
          content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Erro ao criar categoria",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
      @ApiResponse(responseCode = "403", description = "Unauthorized")
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryResponseDTO save(@RequestBody @Valid NewCategoryDTO dto) {
    return mapper.toDTO(categoryUseCase.save(mapper.toModel(dto)));
  }

  @Operation(summary = "Busca a categoria por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar categoria pelo ID",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
  @GetMapping("/{id}")
  public CategoryResponseDTO findById(@PathVariable Long id) {
    return mapper.toDTO(categoryUseCase.findById(id));
  }

  @Operation(summary = "Busca lista de categorias paginada")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categorias encontradas com sucesso",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar categoria pelo ID",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
  @GetMapping
  public Page<CategoryResponseDTO> findAll(Category category, Pageable pageable) {
    return categoryUseCase.findAll(category, pageable).map(mapper::toDTO);
  }

  @Operation(summary = "Busca lista de categorias")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categorias encontradas com sucesso",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar categoria pelo ID",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
  @GetMapping("/list")
  public List<CategoryResponseDTO> listAll() {
    return categoryUseCase.listAll().stream().map(mapper::toDTO).toList();
  }

  @Operation(summary = "Atualiza uma categoria")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
          content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Erro ao atualizar categoria",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Category not found")
  })
  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public CategoryResponseDTO update(@RequestBody @Valid CategoryPayloadDTO dto) {
    return mapper.toDTO(categoryUseCase.save(mapper.toModel(dto)));
  }

  @Operation(summary = "Deleta a categoria por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar categoria pelo ID",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Long id) {
    categoryUseCase.deleteById(id);
  }

}