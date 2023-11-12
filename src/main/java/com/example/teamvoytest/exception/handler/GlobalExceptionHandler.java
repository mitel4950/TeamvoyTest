package com.example.teamvoytest.exception.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .toList();

    body.put("messages", errors);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
    log.error("An unexpected error occurred", ex);
    HttpStatus status = resolveAnnotatedResponseStatus(ex);
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", ex.getMessage());
    return new ResponseEntity<>(body, status);
  }


  private HttpStatus resolveAnnotatedResponseStatus(Exception ex) {
    ResponseStatus annotation =
        AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
    if (annotation != null) {
      return annotation.value();
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

}
