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
      return switch (category) {
          case REAL_ESTATE -> createdAt.plus(properties.realEstate());
          case VEHICLE -> createdAt.plus(properties.vehicle());
          case SHOPPING -> createdAt.plus(properties.shopping());
          case OTHER -> createdAt.plus(properties.other());
      };
  }
}
