package br.com.msodrej.myfinance.domain.enums;

import static java.lang.String.format;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SystemErrorMessage {
  ERR001("ERR001", "User with id %s not found"),
  ERR002("ERR002", "User with username %s not found"),
  ERR003("ERR003", "Role with id %s not found"),
  ERR004("ERR004", "Financial with id %s not found"),
  ERR005("ERR005", "Transaction with id %s not found"),
  ;

  private final String code;
  private final String message;

  public String getFormattedMessage(Object... args) {
    return format(format("%s - %s", code, message), args);
  }
}