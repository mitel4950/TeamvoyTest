package com.example.teamvoytest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ProductStatusException extends RuntimeException {

  public ProductStatusException(String message) {
    super(message);
  }
}
