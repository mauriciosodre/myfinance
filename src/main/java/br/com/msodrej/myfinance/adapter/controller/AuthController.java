package br.com.msodrej.myfinance.adapter.controller;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

import br.com.msodrej.myfinance.adapter.config.security.AuthenticationService;
import br.com.msodrej.myfinance.adapter.dto.auth.AuthenticationResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.auth.ForgotPasswordDTO;
import br.com.msodrej.myfinance.adapter.dto.auth.NewPasswordDTO;
import br.com.msodrej.myfinance.adapter.dto.auth.SignInDTO;
import br.com.msodrej.myfinance.adapter.dto.user.UserResponseDTO;
import br.com.msodrej.myfinance.adapter.mapper.UserDTOMapper;
import br.com.msodrej.myfinance.domain.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationService authenticationService;
  private final UserDTOMapper userDTOMapper;

  @Operation(summary = "Faz o login de um usuário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao logar usuário",
          content = @Content)})
  @PostMapping
  @ResponseStatus(OK)
  public AuthenticationResponseDTO signin(@RequestBody SignInDTO request) {
    return authenticationService.signin(request);
  }

  @Operation(summary = "Busca o usuário autenticado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Busca o usuário autenticado",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao buscar usuário autenticado",
          content = @Content)})
  @GetMapping
  @ResponseStatus(OK)
  public UserResponseDTO getAuthenticatedUser() {
    var principal = SecurityUtils.getCurrentUser();
    return userDTOMapper.toDTO(principal.getUser());
  }

  @Operation(summary = "Atualiza a senha de um usuário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Senha atualizada com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao atualizar senha",
          content = @Content)})
  @PutMapping("/new-password")
  @ResponseStatus(ACCEPTED)
  public void updatePassword(@Valid @RequestBody NewPasswordDTO data) {
    authenticationService.updatePassword(data.oldPassword(), data.newPassword());
  }

  @Operation(summary = "Envia um email com uma nova senha para o usuário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Email com nova senha enviado com sucesso",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponseDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Erro ao enviar email com nova senha",
          content = @Content)})
  @PostMapping("/forgot-password")
  @ResponseStatus(ACCEPTED)
  public void forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
    authenticationService.forgotPassword(forgotPasswordDTO.username());
  }

}
