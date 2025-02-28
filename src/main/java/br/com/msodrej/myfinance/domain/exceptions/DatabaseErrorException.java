package br.com.msodrej.myfinance.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DatabaseErrorException extends RuntimeException {

    public DatabaseErrorException(String msg) {
        super(msg);
    }
}