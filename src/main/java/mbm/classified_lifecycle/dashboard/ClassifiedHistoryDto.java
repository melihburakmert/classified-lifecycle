package mbm.classified_lifecycle.dashboard;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import mbm.classified_lifecycle.common.ClassifiedStatus;

@Builder
public record ClassifiedHistoryDto(
    UUID id,
    UUID classifiedId,
    ClassifiedStatus previousStatus,
    ClassifiedStatus newStatus,
    Instant changedAt) {
}
