package mbm.classified_lifecycle.common;

import java.util.UUID;
import mbm.classified_lifecycle.common.exception.AbstractApiException;
import org.springframework.http.HttpStatus;

public class ClassifiedNotFoundException extends AbstractApiException {

  private static final String DETAILS = "Classified ad with id %s not found";
  private static final String MESSAGE = "Classified ad not found";
  private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

  private final UUID id;

  public ClassifiedNotFoundException(final UUID id) {
    this.id = id;
  }

  @Override
  public String getDetails() {
    return String.format(DETAILS, id);
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
