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

  @PostMapping
  @ResponseStatus(OK)
  public AuthenticationResponseDTO signin(@RequestBody SignInDTO request) {
    return authenticationService.signin(request);
  }

  @GetMapping
  @ResponseStatus(OK)
  public UserResponseDTO getAuthenticatedUser() {
    var principal = authenticationService.getAuthenticatedUser();
    return userDTOMapper.toDTO(principal.getUser());
  }

  @PutMapping("/new-password")
  @ResponseStatus(ACCEPTED)
  public void updatePassword(@Valid @RequestBody NewPasswordDTO data) {
    authenticationService.updatePassword(data.oldPassword(), data.newPassword());
  }

  @PostMapping("/forgot-password")
  @ResponseStatus(ACCEPTED)
  public void forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
    authenticationService.forgotPassword(forgotPasswordDTO.username());
  }

}
