package br.com.msodrej.myfinance.domain.model;

import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

  private UUID id;

  private String username;

  private String password;

  private String name;

  private String email;

  private String phone;

  private Boolean active;

  private Set<Role> roles;

}
