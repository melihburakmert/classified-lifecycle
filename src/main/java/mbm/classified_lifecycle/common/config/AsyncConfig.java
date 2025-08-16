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
    executor.setCorePoolSize(threadPoolProperties.corePoolSize());
    executor.setMaxPoolSize(threadPoolProperties.maxPoolSize());
    executor.setQueueCapacity(threadPoolProperties.queueCapacity());
    executor.setThreadNamePrefix(threadPoolProperties.threadNamePrefix());
    executor.initialize();
    return executor;
  }
}
