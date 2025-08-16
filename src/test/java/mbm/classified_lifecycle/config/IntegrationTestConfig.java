package mbm.classified_lifecycle.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestConfig {
  // No Testcontainers needed for H2 in-memory tests.
}
