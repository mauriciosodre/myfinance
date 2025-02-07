package br.com.msodrej.myfinance.adapter.config.security;

import br.com.msodrej.myfinance.adapter.dto.auth.AuthenticationResponseDTO;
import br.com.msodrej.myfinance.adapter.dto.auth.SignInDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponseDTO signin(SignInDTO request) {
    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    var principal = (UserDetails) authentication.getPrincipal();
    var jwt = jwtService.generateToken(principal);
    return new AuthenticationResponseDTO(jwt);
  }

  public UserPrincipal getAuthenticatedUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
      return (UserPrincipal) authentication.getPrincipal();
    }
    return null;
  }

  public void updatePassword(String oldPassword, String newPassword) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Transactional
  public void forgotPassword(String username) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

}