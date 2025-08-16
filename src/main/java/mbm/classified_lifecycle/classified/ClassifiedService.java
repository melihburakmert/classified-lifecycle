package mbm.classified_lifecycle.classified;

import java.util.UUID;

public interface ClassifiedService {

  ClassifiedDto createClassified(ClassifiedRequestDto classifiedRequestDto);

  ClassifiedDto activateClassified(UUID id);

  ClassifiedDto deactivateClassified(UUID id);
}
