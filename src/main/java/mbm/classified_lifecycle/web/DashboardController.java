package mbm.classified_lifecycle.web;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mbm.classified_lifecycle.dashboard.ClassifiedDashboardService;
import mbm.classified_lifecycle.dashboard.ClassifiedHistoryDto;
import mbm.classified_lifecycle.dashboard.ClassifiedStatisticsDto;
import mbm.classified_lifecycle.web.api.DashboardApiDelegate;
import mbm.classified_lifecycle.web.mapper.ClassifiedHistoryResponseMapper;
import mbm.classified_lifecycle.web.mapper.ClassifiedStatisticsResponseMapper;
import mbm.classified_lifecycle.web.model.ClassifiedStatisticsResponse;
import mbm.classified_lifecycle.web.model.ClassifiedStatusHistoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardController implements DashboardApiDelegate {

  private final ClassifiedDashboardService classifiedDashboardService;
  private final ClassifiedHistoryResponseMapper classifiedHistoryResponseMapper;
  private final ClassifiedStatisticsResponseMapper classifiedStatisticsResponseMapper;

  @Override
  public ResponseEntity<List<ClassifiedStatusHistoryResponse>> getClassifiedStatusHistory(
      final UUID id) {

    final List<ClassifiedHistoryDto> historyDtos =
        classifiedDashboardService.getClassifiedStatusHistory(id);
    final List<ClassifiedStatusHistoryResponse> responses =
        classifiedHistoryResponseMapper.mapToList(historyDtos);

    return ResponseEntity.ok(responses);
  }

  @Override
  public ResponseEntity<ClassifiedStatisticsResponse> getClassifiedStatistics() {

    final ClassifiedStatisticsDto statisticsDto =
        classifiedDashboardService.getClassifiedStatistics();
    final ClassifiedStatisticsResponse response =
        classifiedStatisticsResponseMapper.map(statisticsDto);

    return ResponseEntity.ok(response);
  }
}
