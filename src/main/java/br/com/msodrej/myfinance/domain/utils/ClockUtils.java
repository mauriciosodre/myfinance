package br.com.msodrej.myfinance.domain.utils;

import java.time.LocalDate;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class ClockUtils {

  public Date newDate() {
    return new Date();
  }

  public Date newDate(long date) {
    return new Date(date);
  }

  public LocalDate newLocalDate() {
    return LocalDate.now();
  }

  //Implementar outros métodos relacionados a data e hora

}