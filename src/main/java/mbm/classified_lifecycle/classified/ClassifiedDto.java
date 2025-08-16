package mbm.classified_lifecycle.classified;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import mbm.classified_lifecycle.common.ClassifiedCategory;
import mbm.classified_lifecycle.common.ClassifiedStatus;

@Builder
public record ClassifiedDto(
    UUID id,
    String title,
    String description,
    ClassifiedCategory category,
    ClassifiedStatus status,
    Instant createdAt,
    Instant expiresAt) {
}
