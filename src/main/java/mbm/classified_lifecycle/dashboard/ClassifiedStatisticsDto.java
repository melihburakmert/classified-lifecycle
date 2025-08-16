package mbm.classified_lifecycle.dashboard;

import java.util.Map;
import lombok.Builder;

@Builder
public record ClassifiedStatisticsDto(Map<String, Integer> countByStatus) {
}
