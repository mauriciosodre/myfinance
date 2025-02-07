package br.com.msodrej.myfinance.adapter.config.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher.Builder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final UserDetailsService userDetailsService;

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private static final String[] PUBLIC_MATCHERS_POST = {"/auth", "/auth/forgot-password", "/users"};

  private static final String[] PUBLIC_MATCHERS_PUT = {};

  private static final String[] PUBLIC_MATCHERS_GET = {};

  private static final String[] WHITELIST = {
      // -- swagger ui
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/webjars/**",
      "/v3/api-docs/**",
      "/swagger-ui/**",

      // -- actuators
      "/actuator/**"
  };

  private MvcRequestMatcher[] mvcRequestMatchers(HandlerMappingIntrospector introspector,
      HttpMethod httpMethod, String... patterns) {
    var mvc = new Builder(introspector);
    if (Objects.isNull(httpMethod)) {
      return Arrays.stream(patterns).map(mvc::pattern)
          .toArray(MvcRequestMatcher[]::new);
    } else {
      return Arrays.stream(patterns).map(pattern -> mvc.pattern(httpMethod, pattern))
          .toArray(MvcRequestMatcher[]::new);
    }
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
      throws Exception {
    return http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
            corsConfigurationSource())).csrf(AbstractHttpConfigurer::disable).sessionManagement(
            httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                STATELESS)).authorizeHttpRequests(
            authorize -> authorize.requestMatchers(mvcRequestMatchers(introspector, null, WHITELIST))
                .permitAll()
                .requestMatchers(mvcRequestMatchers(introspector, GET, PUBLIC_MATCHERS_GET))
                .permitAll()
                .requestMatchers(mvcRequestMatchers(introspector, POST, PUBLIC_MATCHERS_POST))
                .permitAll()
                .requestMatchers(mvcRequestMatchers(introspector, PUT, PUBLIC_MATCHERS_PUT))
                .permitAll().anyRequest().authenticated())
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Collections.singletonList("*"));
    configuration.setAllowedMethods(
        Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
    configuration.setExposedHeaders(Collections.singletonList("x-auth-token"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}