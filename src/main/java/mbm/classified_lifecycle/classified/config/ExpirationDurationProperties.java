package mbm.classified_lifecycle.classified.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "classified.expiration.duration")
public record ExpirationDurationProperties(
    Duration realEstate,
    Duration vehicle,
    Duration shopping,
    Duration other) {
}
