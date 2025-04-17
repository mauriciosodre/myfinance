package br.com.msodrej.myfinance.adapter.config.security;

import static br.com.msodrej.myfinance.domain.enums.SystemErrorMessage.ERR012;

import br.com.msodrej.myfinance.adapter.dto.auth.AuthenticationResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.auth.SignInDTO;
import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import br.com.msodrej.myfinance.domain.usecase.UserUseCase;
import br.com.msodrej.myfinance.domain.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserUseCase userUseCase;

  public AuthenticationResponseDTO signin(SignInDTO request) {
    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    var principal = (UserDetails) authentication.getPrincipal();
    var jwt = jwtService.generateToken(principal);
    return new AuthenticationResponseDTO(jwt);
  }

  @Transactional
  public void updatePassword(String oldPassword, String newPassword) {
    var principal = SecurityUtils.getCurrentUser();
    var user = principal.getUser();

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new SystemErrorException(ERR012.getFormattedMessage());
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userUseCase.save(user);
  }

  @Transactional
  public void forgotPassword(String username) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

}