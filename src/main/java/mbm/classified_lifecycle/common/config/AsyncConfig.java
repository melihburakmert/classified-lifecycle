package mbm.classified_lifecycle.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig {

  private final ThreadPoolProperties threadPoolProperties;

  @Bean(name = "expirationTaskExecutor")
  public TaskExecutor taskExecutor() {

    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
    executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
    executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
    executor.setThreadNamePrefix(threadPoolProperties.getThreadNamePrefix());
    executor.initialize();
    return executor;
  }
}
