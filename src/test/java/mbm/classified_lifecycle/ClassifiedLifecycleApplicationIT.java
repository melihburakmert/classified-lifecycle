package mbm.classified_lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.test.ApplicationModuleTest;

@SpringBootTest
@ApplicationModuleTest
class ClassifiedLifecycleApplicationIT {

  @Test
  void contextLoads() {
  }

  @Test
  void verifyModules() {
    final var modules = ApplicationModules.of(ClassifiedLifecycleApplication.class);
    modules.verify();
    modules.forEach(System.out::println);

    new Documenter(modules)
        .writeModulesAsPlantUml()
        .writeIndividualModulesAsPlantUml();
  }
}
