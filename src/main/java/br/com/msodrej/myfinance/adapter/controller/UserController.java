package br.com.msodrej.myfinance.adapter.controller;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import br.com.msodrej.myfinance.adapter.dto.user.NewUserDTO;
import br.com.msodrej.myfinance.adapter.dto.user.UserPayloadDTO;
import br.com.msodrej.myfinance.adapter.dto.user.UserResponseDTO;
import br.com.msodrej.myfinance.adapter.mapper.UserDTOMapper;
import br.com.msodrej.myfinance.domain.usecase.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/users")
public class UserController {

  private final UserUseCase useCase;
  private final UserDTOMapper mapper;

  @Operation(summary = "Cria um novo usuário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao criar usuário",
          content = @Content)})
  @PostMapping
  @ResponseStatus(CREATED)
  public UserResponseDTO save(@RequestBody NewUserDTO user) {
    return mapper.toDTO(useCase.save(mapper.toModel(user)));
  }

  @Operation(summary = "Atualiza um usuário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao atualizar usuário",
          content = @Content)})
  @PutMapping
  @ResponseStatus(OK)
  public UserResponseDTO update(@RequestBody UserPayloadDTO user) {
    return mapper.toDTO(useCase.update(mapper.toModel(user)));
  }

  @Operation(summary = "Deleta um usuário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao deletar usuário",
          content = @Content)})
  @DeleteMapping
  @ResponseStatus(NO_CONTENT)
  public void deleteById(UUID id) {
    useCase.deleteById(id);
  }

  @Operation(summary = "Cria um novo usuário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Usuário ativado/desativado com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao ativar/desativar usuário",
          content = @Content)})
  @PatchMapping("/{id}/activate")
  @ResponseStatus(NO_CONTENT)
  public void activate(@PathVariable UUID id) {
    useCase.activate(id);
  }

  @Operation(summary = "Busca um usuário pelo ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar usuário pelo ID",
          content = @Content)})
  @GetMapping("/{id}")
  @ResponseStatus(OK)
  public UserResponseDTO findById(@PathVariable UUID id) {
    return mapper.toDTO(useCase.findById(id));
  }

  @Operation(summary = "Buscar usuários")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar usuários",
          content = @Content)})
  @GetMapping
  @ResponseStatus(OK)
  public Page<UserResponseDTO> findAll(Pageable pageable) {
    return useCase.findAll(pageable).map(mapper::toDTO);
  }

}
