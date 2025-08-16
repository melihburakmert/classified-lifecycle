package mbm.classified_lifecycle.classified.scheduled.config;

import lombok.RequiredArgsConstructor;
import mbm.classified_lifecycle.classified.scheduled.ClassifiedExpirationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ExpirationSchedulingConfig implements SchedulingConfigurer {

  private final ExpirationServiceProperties expirationServiceProperties;
  private final ClassifiedExpirationService classifiedExpirationService;

  @Override
  public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.addCronTask(
        classifiedExpirationService::expireClassifieds,
        expirationServiceProperties.getCronExpression());
  }
}
