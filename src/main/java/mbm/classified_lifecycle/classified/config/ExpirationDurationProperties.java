package mbm.classified_lifecycle.classified.config;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "classified.expiration.duration")
public class ExpirationDurationProperties {
  private Duration realEstate;
  private Duration vehicle;
  private Duration shopping;
  private Duration other;
}
