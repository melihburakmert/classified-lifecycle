package mbm.classified_lifecycle.classified;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import mbm.classified_lifecycle.common.ClassifiedCategory;
import mbm.classified_lifecycle.common.ClassifiedStatus;

@Value
@Builder
public class ClassifiedDto {
  UUID id;
  String title;
  String description;
  ClassifiedCategory category;
  ClassifiedStatus status;
  Instant createdAt;
  Instant expiresAt;
}
