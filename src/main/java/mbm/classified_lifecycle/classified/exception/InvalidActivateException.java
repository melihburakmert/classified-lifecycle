package mbm.classified_lifecycle.classified.exception;

import java.util.UUID;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import mbm.classified_lifecycle.common.exception.AbstractApiException;
import org.springframework.http.HttpStatus;

public class InvalidActivateException extends AbstractApiException {

  private static final String DETAILS =
      "Classified ad with id %s cannot be activated. Current status: %s";
  private static final String MESSAGE = "Cannot activate classified ad";
  private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

  private final UUID id;
  private final ClassifiedStatus currentStatus;

  public InvalidActivateException(final UUID id, final ClassifiedStatus currentStatus) {
    this.id = id;
    this.currentStatus = currentStatus;
  }

  @Override
  public String getDetails() {
    return String.format(DETAILS, id, currentStatus);
  }

  @Override
  public String getMessage() {
    return MESSAGE;
  }

  @Override
  public HttpStatus getStatus() {
    return STATUS;
  }
}
