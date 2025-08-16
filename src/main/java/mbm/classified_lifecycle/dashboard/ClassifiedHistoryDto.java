package mbm.classified_lifecycle.dashboard;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import mbm.classified_lifecycle.common.ClassifiedStatus;

@Value
@Builder
public class ClassifiedHistoryDto {
  UUID id;
  UUID classifiedId;
  ClassifiedStatus previousStatus;
  ClassifiedStatus newStatus;
  Instant changedAt;
}
