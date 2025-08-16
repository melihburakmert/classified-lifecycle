package mbm.classified_lifecycle.dashboard;

import java.util.List;
import java.util.UUID;

public interface ClassifiedDashboardService {

  List<ClassifiedHistoryDto> getClassifiedStatusHistory(UUID id);

  ClassifiedStatisticsDto getClassifiedStatistics();
}
