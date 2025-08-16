package mbm.classified_lifecycle.classified.persistence.projection;

import java.util.UUID;
import mbm.classified_lifecycle.common.ClassifiedStatus;

public interface ClassifiedStatusProjection {
  UUID getId();

  ClassifiedStatus getStatus();
}
