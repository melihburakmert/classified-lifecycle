package mbm.classified_lifecycle.classified.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import mbm.classified_lifecycle.classified.config.ExpirationDurationProperties;
import mbm.classified_lifecycle.common.ClassifiedCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassifiedExpirationCalculatorUT {

  @Mock ExpirationDurationProperties properties;

  private ClassifiedExpirationCalculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new ClassifiedExpirationCalculator(properties);
  }

  @Test
  void test_calculateExpirationDate_realEstate() {
    // GIVEN
    final Instant now = Instant.now();
    final Duration duration = Duration.ofDays(30);
    when(properties.realEstate()).thenReturn(duration);

    // WHEN
    final Instant result = calculator.calculateExpirationDate(ClassifiedCategory.REAL_ESTATE, now);

    // THEN
    assertThat(result).isEqualTo(now.plus(duration));
  }

  @Test
  void test_calculateExpirationDate_vehicle() {
    // GIVEN
    final Instant now = Instant.now();
    final Duration duration = Duration.ofDays(15);
    when(properties.vehicle()).thenReturn(duration);

    // WHEN
    final Instant result = calculator.calculateExpirationDate(ClassifiedCategory.VEHICLE, now);

    // THEN
    assertThat(result).isEqualTo(now.plus(duration));
  }

  @Test
  void test_calculateExpirationDate_shopping() {
    // GIVEN
    final Instant now = Instant.now();
    final Duration duration = Duration.ofDays(7);
    when(properties.shopping()).thenReturn(duration);

    // WHEN
    final Instant result = calculator.calculateExpirationDate(ClassifiedCategory.SHOPPING, now);

    // THEN
    assertThat(result).isEqualTo(now.plus(duration));
  }

  @Test
  void test_calculateExpirationDate_other() {
    // GIVEN
    final Instant now = Instant.now();
    final Duration duration = Duration.ofDays(3);
    when(properties.other()).thenReturn(duration);

    // WHEN
    final Instant result = calculator.calculateExpirationDate(ClassifiedCategory.OTHER, now);

    // THEN
    assertThat(result).isEqualTo(now.plus(duration));
  }
}
