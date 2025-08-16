package mbm.classified_lifecycle.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.createList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import mbm.classified_lifecycle.dashboard.ClassifiedDashboardService;
import mbm.classified_lifecycle.dashboard.ClassifiedHistoryDto;
import mbm.classified_lifecycle.dashboard.ClassifiedStatisticsDto;
import mbm.classified_lifecycle.web.mapper.ClassifiedHistoryResponseMapper;
import mbm.classified_lifecycle.web.mapper.ClassifiedStatisticsResponseMapper;
import mbm.classified_lifecycle.web.model.ClassifiedStatisticsResponse;
import mbm.classified_lifecycle.web.model.ClassifiedStatusHistoryResponse;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DashboardControllerUT {

  @Mock private ClassifiedDashboardService classifiedDashboardService;
  @Mock private ClassifiedHistoryResponseMapper classifiedHistoryResponseMapper;
  @Mock private ClassifiedStatisticsResponseMapper classifiedStatisticsResponseMapper;

  private DashboardController dashboardController;

  @BeforeEach
  void setUp() {
    dashboardController =
        new DashboardController(
            classifiedDashboardService,
            classifiedHistoryResponseMapper,
            classifiedStatisticsResponseMapper);
  }

  @Test
  void test_getClassifiedStatusHistory() {
    // GIVEN
    final UUID id = UUID.randomUUID();
    final List<ClassifiedHistoryDto> historyDtos = createList(ClassifiedHistoryDto.class);
    final List<ClassifiedStatusHistoryResponse> responses =
        createList(ClassifiedStatusHistoryResponse.class);

    when(classifiedDashboardService.getClassifiedStatusHistory(id)).thenReturn(historyDtos);
    when(classifiedHistoryResponseMapper.mapToList(historyDtos)).thenReturn(responses);

    // WHEN
    final ResponseEntity<List<ClassifiedStatusHistoryResponse>> responseEntity =
        dashboardController.getClassifiedStatusHistory(id);

    // THEN
    assertThat(responseEntity)
        .isNotNull()
        .satisfies(
            response -> {
              assertThat(response.getStatusCodeValue()).isEqualTo(200);
              assertThat(response.getBody()).isNotNull().isEqualTo(responses);
            });
  }

  @Test
  void test_getClassifiedStatistics() {
    // GIVEN
    final ClassifiedStatisticsDto statisticsDto = Instancio.create(ClassifiedStatisticsDto.class);
    final ClassifiedStatisticsResponse response =
        Instancio.create(ClassifiedStatisticsResponse.class);

    when(classifiedDashboardService.getClassifiedStatistics()).thenReturn(statisticsDto);
    when(classifiedStatisticsResponseMapper.map(statisticsDto)).thenReturn(response);

    // WHEN
    final ResponseEntity<ClassifiedStatisticsResponse> responseEntity =
        dashboardController.getClassifiedStatistics();

    // THEN
    assertThat(responseEntity)
        .isNotNull()
        .satisfies(
            res -> {
              assertThat(res.getStatusCodeValue()).isEqualTo(200);
              assertThat(res.getBody()).isNotNull().isEqualTo(response);
            });
  }
}
