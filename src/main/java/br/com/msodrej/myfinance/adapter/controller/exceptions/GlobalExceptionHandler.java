package br.com.msodrej.myfinance.adapter.controller.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import br.com.msodrej.myfinance.domain.exceptions.SystemErrorException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

  //handle specific exceptions
  @ExceptionHandler(SystemErrorException.class)
  public ResponseEntity<ErrorDetails> handleSystemErrorException(
      SystemErrorException exception, HttpServletRequest request) {
    return ResponseEntity.status(BAD_REQUEST).body(ErrorDetails
        .builder()
        .cod(BAD_REQUEST.value())
        .status(BAD_REQUEST)
        .dateTime(LocalDateTime.now(ZONE_ID))
        .message(exception.getMessage())
        .path(request.getRequestURI())
        .build());
  }

  //handle global exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
      HttpServletRequest request) {
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ErrorDetails
        .builder()
        .cod(INTERNAL_SERVER_ERROR.value())
        .status(INTERNAL_SERVER_ERROR)
        .dateTime(LocalDateTime.now(ZONE_ID))
        .message(exception.getMessage())
        .path(request.getRequestURI())
        .build());
  }
}