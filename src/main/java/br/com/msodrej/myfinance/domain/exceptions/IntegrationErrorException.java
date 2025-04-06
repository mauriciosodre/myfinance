package br.com.msodrej.myfinance.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IntegrationErrorException extends RuntimeException {

    public IntegrationErrorException(String msg) {
        super(msg);
    }
}