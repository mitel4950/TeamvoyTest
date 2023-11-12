package com.example.teamvoytest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class OrderStatusException extends RuntimeException {

  public OrderStatusException(String message) {
    super(message);
  }
}
