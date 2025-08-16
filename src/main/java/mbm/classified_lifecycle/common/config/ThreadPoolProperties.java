package mbm.classified_lifecycle.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "classified.thread-pool")
public record ThreadPoolProperties(
    int corePoolSize,
    int maxPoolSize,
    int queueCapacity,
    String threadNamePrefix) {
}
