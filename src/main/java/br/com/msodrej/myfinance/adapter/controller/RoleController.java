package br.com.msodrej.myfinance.adapter.controller;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import br.com.msodrej.myfinance.adapter.dto.role.NewRoleDTO;
import br.com.msodrej.myfinance.adapter.dto.role.RolePayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.role.RoleResponseDTO;
import br.com.msodrej.myfinance.adapter.mapper.RoleDTOMapper;
import br.com.msodrej.myfinance.domain.usecase.RoleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/roles")
public class RoleController {

  private final RoleUseCase useCase;
  private final RoleDTOMapper mapper;

  @Operation(summary = "Cria uma nova role")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Role criada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = RoleResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao criar role",
          content = @Content)})
  @PostMapping
  @ResponseStatus(CREATED)
  public RoleResponseDTO save(@RequestBody NewRoleDTO role) {
    return mapper.toDTO(useCase.save(mapper.toModel(role)));
  }

  @Operation(summary = "Atualiza um role")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Role atualizada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = RoleResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao atualizar role",
          content = @Content)})
  @PutMapping
  @ResponseStatus(OK)
  public RoleResponseDTO update(@RequestBody RolePayloadDTO role) {
    return mapper.toDTO(useCase.save(mapper.toModel(role)));
  }

  @Operation(summary = "Deleta uma role")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Role deletada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = RoleResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao deletar role",
          content = @Content)})
  @DeleteMapping
  @ResponseStatus(NO_CONTENT)
  public void deleteById(Long id) {
    useCase.delete(id);
  }

  @Operation(summary = "Busca uma role pelo ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Role encontrada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = RoleResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar role pelo ID",
          content = @Content)})
  @GetMapping("/{id}")
  @ResponseStatus(OK)
  public RoleResponseDTO findById(@PathVariable Long id) {
    return mapper.toDTO(useCase.findById(id));
  }

  @Operation(summary = "Buscar roles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Roles encontradas com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = RoleResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar roles",
          content = @Content)})
  @GetMapping
  @ResponseStatus(OK)
  public Page<RoleResponseDTO> findAll(Pageable pageable) {
    return useCase.findAll(pageable).map(mapper::toDTO);
  }

}
