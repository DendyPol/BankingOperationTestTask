package ru.polovinko.bankingservice.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.polovinko.bankingservice.exception.AlreadyExistException;
import ru.polovinko.bankingservice.exception.EntityNotFoundException;
import ru.polovinko.bankingservice.exception.RefreshTokenException;

@RestControllerAdvice
public class WebAppExceptionHandler {
  @ExceptionHandler(value = RefreshTokenException.class)
  public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(RefreshTokenException ex, WebRequest webRequest) {
    return buildResponse(HttpStatus.FORBIDDEN, ex, webRequest);
  }

  @ExceptionHandler(value = AlreadyExistException.class)
  public ResponseEntity<ErrorResponseBody> alreadyExistsHandler(AlreadyExistException ex, WebRequest request) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex, request);
  }

  @ExceptionHandler(value = EntityNotFoundException.class)
  public ResponseEntity<ErrorResponseBody> notFoundHandler(EntityNotFoundException ex, WebRequest request) {
    return buildResponse(HttpStatus.NOT_FOUND, ex, request);
  }

  private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus httpStatus, Exception ex, WebRequest webRequest) {
    return ResponseEntity.status(httpStatus)
      .body(ErrorResponseBody.builder()
        .message(ex.getMessage())
        .description(webRequest.getDescription(false))
        .build());
  }
}
