package mbm.classified_lifecycle.classified.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import mbm.classified_lifecycle.classified.config.ExpirationDurationProperties;
import mbm.classified_lifecycle.common.ClassifiedCategory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassifiedExpirationCalculator {

  private final ExpirationDurationProperties properties;

  public Instant calculateExpirationDate(
      final ClassifiedCategory category, final Instant createdAt) {
    switch (category) {
      case REAL_ESTATE:
        return createdAt.plus(properties.getRealEstate());
      case VEHICLE:
        return createdAt.plus(properties.getVehicle());
      case SHOPPING:
        return createdAt.plus(properties.getShopping());
      case OTHER:
        return createdAt.plus(properties.getOther());
      default:
        throw new IllegalArgumentException("Unknown category: " + category);
    }
  }
}
