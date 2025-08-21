package mbm.classified_lifecycle.common;

import org.springframework.http.HttpStatus;

public abstract class AbstractApiException extends RuntimeException {

  public abstract String getDetails();

  @Override
  public abstract String getMessage();

  public abstract HttpStatus getStatus();
}
