package mbm.classified_lifecycle.dashboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Instancio.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;
import mbm.classified_lifecycle.common.ClassifiedNotFoundException;
import mbm.classified_lifecycle.dashboard.ClassifiedDashboardService;
import mbm.classified_lifecycle.dashboard.ClassifiedHistoryDto;
import mbm.classified_lifecycle.dashboard.ClassifiedStatisticsDto;
import mbm.classified_lifecycle.dashboard.persistence.mapper.ClassifiedHistoryProjectionMapper;
import mbm.classified_lifecycle.dashboard.persistence.mapper.ClassifiedStatusCountProjectionMapper;
import mbm.classified_lifecycle.dashboard.persistence.projection.ClassifiedHistoryProjection;
import mbm.classified_lifecycle.dashboard.persistence.projection.ClassifiedStatusCountProjection;
import mbm.classified_lifecycle.dashboard.persistence.repository.ClassifiedHistoryReadOnlyRepository;
import mbm.classified_lifecycle.dashboard.persistence.repository.ClassifiedReadOnlyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassifiedDashboardServiceUT {

  @Mock private ClassifiedReadOnlyRepository classifiedRepository;
  @Mock private ClassifiedHistoryReadOnlyRepository classifiedHistoryRepository;
  @Mock private ClassifiedHistoryProjectionMapper classifiedHistoryEntityMapper;
  @Mock private ClassifiedStatusCountProjectionMapper classifiedStatusCountProjectionMapper;

  private ClassifiedDashboardService classifiedDashboardService;

  @BeforeEach
  void setUp() {
    classifiedDashboardService =
        new ClassifiedDashboardServiceImp(
            classifiedRepository,
            classifiedHistoryRepository,
            classifiedHistoryEntityMapper,
            classifiedStatusCountProjectionMapper);
  }

  @Test
  void test_getClassifiedStatusHistory_success() {
    final UUID id = UUID.randomUUID();
    final List<ClassifiedHistoryProjection> projections =
        List.of(mock(ClassifiedHistoryProjection.class), mock(ClassifiedHistoryProjection.class));
    final List<ClassifiedHistoryDto> dtos = ofList(ClassifiedHistoryDto.class).size(2).create();

    when(classifiedRepository.existsById(id)).thenReturn(true);
    when(classifiedHistoryRepository.findByClassifiedIdOrderByChangedAtDesc(id))
        .thenReturn(projections);
    when(classifiedHistoryEntityMapper.map(projections)).thenReturn(dtos);

    final List<ClassifiedHistoryDto> result =
        classifiedDashboardService.getClassifiedStatusHistory(id);

    assertThat(result).isEqualTo(dtos);
    verify(classifiedRepository).existsById(id);
    verify(classifiedHistoryRepository).findByClassifiedIdOrderByChangedAtDesc(id);
    verify(classifiedHistoryEntityMapper).map(projections);
  }

  @Test
  void test_getClassifiedStatusHistory_notFound() {
    final UUID id = UUID.randomUUID();
    when(classifiedRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> classifiedDashboardService.getClassifiedStatusHistory(id))
        .isInstanceOf(ClassifiedNotFoundException.class);

    verify(classifiedRepository).existsById(id);
    verifyNoInteractions(classifiedHistoryRepository, classifiedHistoryEntityMapper);
  }

  @Test
  void test_getClassifiedStatistics() {
    final List<ClassifiedStatusCountProjection> projections =
        List.of(
            mock(ClassifiedStatusCountProjection.class),
            mock(ClassifiedStatusCountProjection.class));
    final ClassifiedStatisticsDto dto = create(ClassifiedStatisticsDto.class);

    when(classifiedRepository.getClassifiedStatusGroupByCount()).thenReturn(projections);
    when(classifiedStatusCountProjectionMapper.map(projections)).thenReturn(dto);

    final ClassifiedStatisticsDto result = classifiedDashboardService.getClassifiedStatistics();

    assertThat(result).isEqualTo(dto);
    verify(classifiedRepository).getClassifiedStatusGroupByCount();
    verify(classifiedStatusCountProjectionMapper).map(projections);
  }
}
