package mbm.classified_lifecycle.dashboard.persistence.projection;

import java.time.Instant;
import java.util.UUID;
import mbm.classified_lifecycle.common.ClassifiedStatus;

public interface ClassifiedHistoryProjection {
  UUID getId();

  UUID getClassifiedId();

  ClassifiedStatus getPreviousStatus();

  ClassifiedStatus getNewStatus();

  Instant getChangedAt();
}
