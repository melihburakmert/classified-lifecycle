package mbm.classified_lifecycle.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ClassifiedCategory {
  REAL_ESTATE("REAL_ESTATE"),
  VEHICLE("VEHICLE"),
  SHOPPING("SHOPPING"),
  OTHER("OTHER");

  private final String value;

  ClassifiedCategory(final String value) {
    this.value = value;
  }

  @JsonCreator
  public static ClassifiedCategory fromValue(final String value) {
    return Arrays.stream(ClassifiedCategory.values())
        .filter(category -> category.value.equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Invalid ClassifiedCategory value: " + value));
  }
}
