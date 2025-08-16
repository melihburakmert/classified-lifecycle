package mbm.classified_lifecycle.classified.scheduled.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "classified.expiration")
public class ExpirationServiceProperties {
  private String cronExpression;
  private int pageSize;
}
