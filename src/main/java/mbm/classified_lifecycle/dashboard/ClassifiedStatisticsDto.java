package mbm.classified_lifecycle.dashboard;

import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClassifiedStatisticsDto {
  Map<String, Integer> countByStatus;
}
