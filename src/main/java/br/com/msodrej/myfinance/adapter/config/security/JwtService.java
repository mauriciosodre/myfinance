package br.com.msodrej.myfinance.adapter.config.security;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parserBuilder;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static io.jsonwebtoken.io.Decoders.BASE64;

import br.com.msodrej.myfinance.domain.utils.ClockUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final ClockUtils clockUtils;

  @Value("${token.signing.key}")
  private String signingKey;

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateTemporaryToken(UserDetails userDetails, long expirationInMillis) {
    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(clockUtils.newDate(System.currentTimeMillis()))
        .setExpiration(clockUtils.newDate(System.currentTimeMillis() + expirationInMillis))
        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      final var userName = extractUserName(token);
      return (userName.equals(userDetails.getUsername()));
    } catch (Exception e) {
      return false;
    }
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
    final var claims = extractAllClaims(token);
    return claimsResolvers.apply(claims);
  }

  private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
        .setIssuedAt(clockUtils.newDate(System.currentTimeMillis()))
        .setExpiration(clockUtils.newDate(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
        .signWith(getSigningKey(), HS512).compact();
  }

  private Claims extractAllClaims(String token) {
    return parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
  }

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(BASE64.decode(signingKey));
  }
}