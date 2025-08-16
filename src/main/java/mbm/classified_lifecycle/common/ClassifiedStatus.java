package mbm.classified_lifecycle.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ClassifiedStatus {
  PENDING_APPROVAL("PENDING_APPROVAL"),
  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE"),
  DUPLICATE("DUPLICATE"),
  EXPIRED("EXPIRED");

  private final String value;

  ClassifiedStatus(final String value) {
    this.value = value;
  }

  @JsonCreator
  public static ClassifiedStatus fromValue(final String value) {
    return Arrays.stream(ClassifiedStatus.values())
        .filter(category -> category.value.equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Invalid ClassifiedStatus value: " + value));
  }
}
