package mbm.classified_lifecycle.common.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import mbm.classified_lifecycle.common.AbstractApiException;
import mbm.classified_lifecycle.common.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AbstractApiException.class)
  public ResponseEntity<ErrorResponse> handleApiException(final AbstractApiException ex) {
    final ErrorResponse errorResponse =
        new ErrorResponse()
            .status(ex.getStatus().value())
            .message(ex.getMessage())
            .details(ex.getDetails())
            .timestamp(Instant.now());

    return new ResponseEntity<>(errorResponse, ex.getStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      final MethodArgumentNotValidException ex) {
    final ErrorResponse errorResponse = buildValidationErrorResponse(ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  private ErrorResponse buildValidationErrorResponse(final MethodArgumentNotValidException ex) {
    final String message = "Validation failed for the request";

    final String details =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                error ->
                    String.format(
                        "Field '%s' %s (Rejected value: '%s')",
                        error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
            .collect(Collectors.joining("; "));

    return new ErrorResponse()
        .status(HttpStatus.BAD_REQUEST.value())
        .message(message)
        .details(details)
        .timestamp(Instant.now());
  }
}
