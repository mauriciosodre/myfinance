package br.com.msodrej.myfinance.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SystemErrorMessage {
  ERR001("ERR001", "User not found"),
  ;

  private final String code;
  private final String message;

  public String getFormattedMessage() {
    return String.format("%s - %s", code, message);
  }
}