package mbm.classified_lifecycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ClassifiedLifecycleApplication {
  public static void main(final String[] args) {
    SpringApplication.run(ClassifiedLifecycleApplication.class, args);
  }
}
