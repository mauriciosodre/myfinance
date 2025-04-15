package br.com.msodrej.myfinance.adapter.controller;

import br.com.msodrej.myfinance.adapter.controller.exceptions.ErrorDetails;
import br.com.msodrej.myfinance.adapter.dto.transaction.NewTransactionDTO;
import br.com.msodrej.myfinance.adapter.dto.transaction.TransactionPayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.transaction.TransactionResponseDTO;
import br.com.msodrej.myfinance.adapter.mapper.TransactionDTOMapper;
import br.com.msodrej.myfinance.domain.usecase.TransactionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/transactions")
public class TransactionController {

  private final TransactionUseCase transactionUseCase;
  private final TransactionDTOMapper mapper;

  @Operation(summary = "Criar uma nova transação")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Transação criadad",
          content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Erro ao criar transação",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
      @ApiResponse(responseCode = "403", description = "Unauthorized")
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TransactionResponseDTO save(@RequestBody @Valid NewTransactionDTO dto) {
    return mapper.toDTO(transactionUseCase.save(mapper.toModel(dto)));
  }

  @Operation(summary = "Busca a transação por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Transação encontrada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = TransactionResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar transação pelo ID",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
  @GetMapping("/{id}")
  public TransactionResponseDTO findById(@PathVariable Long id) {
    return mapper.toDTO(transactionUseCase.findById(id));
  }

  @Operation(summary = "Atualiza uma transação")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso",
          content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Erro ao atualizar transação",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Transaction not found")
  })
  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public TransactionResponseDTO update(@RequestBody @Valid TransactionPayloadDTO dto) {
    return mapper.toDTO(transactionUseCase.update(mapper.toModel(dto)));
  }

  @Operation(summary = "Deleta a transação por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Transação deletada com sucesso",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar transação pelo ID",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Long id) {
    transactionUseCase.deleteById(id);
  }

  @Operation(summary = "Busca as transações de um financeiro em um período")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Resumo calculado com sucesso",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Erro ao calcular resumo",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
  @GetMapping("/financial/{financialId}/period")
  @ResponseStatus(HttpStatus.OK)
  public List<TransactionResponseDTO> findByPeriod(@PathVariable Long financialId,
      @PathParam(value = "startDate") LocalDate startDate,
      @PathParam(value = "endDate") LocalDate endDate) {
    return transactionUseCase.findByPeriod(financialId, startDate, endDate).stream().map(
        mapper::toDTO).toList();
  }

  @Operation(summary = "Calcula o resumo das transações de um financeiro em um período")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Resumo calculado com sucesso",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Erro ao calcular resumo",
          content = @Content(schema = @Schema(implementation = ErrorDetails.class)))})
  @GetMapping("summary/financial/{financialId}/period")
  @ResponseStatus(HttpStatus.OK)
  public Object calculateFinancialPeriodSummary(@PathVariable Long financialId,
      @PathParam(value = "startDate") LocalDate startDate,
      @PathParam(value = "endDate") LocalDate endDate) {
    return transactionUseCase.calculateFinancialPeriodSummary(financialId, startDate, endDate);
  }

}