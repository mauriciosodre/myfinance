package br.com.msodrej.myfinance.adapter.controller;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import br.com.msodrej.myfinance.adapter.dto.financial.FinancialFilterDTO;
import br.com.msodrej.myfinance.adapter.dto.financial.FinancialPayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.financial.FinancialResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.financial.NewFinancialDTO;
import br.com.msodrej.myfinance.adapter.mapper.FinancialDTOMapper;
import br.com.msodrej.myfinance.domain.usecase.FinancialUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/financials")
@Tag(name = "Financial Controls", description = "Endpoints para gerenciamento de controles financeiros")
public class FinancialController {

  private final FinancialUseCase useCase;
  private final FinancialDTOMapper mapper;

  @Operation(summary = "Cria um novo controle financeiro")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Controle financeiro criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Erro na requisição")
  })
  @PostMapping
  @ResponseStatus(CREATED)
  public FinancialResponseDTO save(@RequestBody NewFinancialDTO financial) {
    return mapper.toDTO(useCase.save(mapper.toModel(financial)));
  }

  @Operation(summary = "Atualiza um financial")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Financial atualizada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = FinancialResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao atualizar financial",
          content = @Content)})
  @PutMapping
  @ResponseStatus(OK)
  public FinancialResponseDTO update(@RequestBody FinancialPayloadDTO financial) {
    return mapper.toDTO(useCase.save(mapper.toModel(financial)));
  }

  @Operation(summary = "Deleta uma financial")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Financial deletada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = FinancialResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao deletar financial",
          content = @Content)})
  @DeleteMapping
  @ResponseStatus(NO_CONTENT)
  public void deleteById(Long id) {
    useCase.delete(id);
  }

  @Operation(summary = "Busca uma financial pelo ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Financial encontrada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = FinancialResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar financial pelo ID",
          content = @Content)})
  @GetMapping("/{id}")
  @ResponseStatus(OK)
  public FinancialResponseDTO findById(@PathVariable Long id) {
    return mapper.toDTO(useCase.findById(id));
  }

  @Operation(summary = "Buscar financials")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Financials encontradas com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = FinancialResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar financials",
          content = @Content)})
  @GetMapping
  @ResponseStatus(OK)
  public Page<FinancialResponseDTO> findAll(FinancialFilterDTO filter, Pageable pageable) {
    var financial = mapper.toModel(filter);
    return useCase.findAll(financial, pageable).map(mapper::toDTO);
  }

  @Operation(summary = "Compartilha um controle financeiro com um usuário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Usuário adicionado ao compartilhamento com sucesso"),
      @ApiResponse(responseCode = "400", description = "Erro ao adicionar usuário do compartilhamento"),
      @ApiResponse(responseCode = "403", description = "Acesso negado")
  })
  @PatchMapping("/{id}/share/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addUserToSharing(@PathVariable Long id, @PathVariable UUID userId) {
    useCase.addUserToSharing(id, userId);
  }

  @Operation(summary = "Remove um usuário do compartilhamento de um controle financeiro")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Usuário removido do compartilhamento com sucesso"),
      @ApiResponse(responseCode = "400", description = "Erro ao remover usuário do compartilhamento"),
      @ApiResponse(responseCode = "403", description = "Acesso negado")
  })
  @DeleteMapping("/{id}/share/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeUserFromSharing(@PathVariable Long id, @PathVariable UUID userId) {
    useCase.removeUserFromSharing(id, userId);
  }

}
