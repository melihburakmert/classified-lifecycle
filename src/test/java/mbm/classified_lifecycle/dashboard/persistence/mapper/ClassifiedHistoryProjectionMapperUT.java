package mbm.classified_lifecycle.dashboard.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import mbm.classified_lifecycle.dashboard.ClassifiedHistoryDto;
import mbm.classified_lifecycle.dashboard.persistence.projection.ClassifiedHistoryProjection;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassifiedHistoryProjectionMapperUT {

  private ClassifiedHistoryProjectionMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ClassifiedHistoryProjectionMapper();
  }

  @Test
  void test_map() {
    // GIVEN
    final ClassifiedHistoryProjection projection1 = mock(ClassifiedHistoryProjection.class);
    final ClassifiedHistoryProjection projection2 = mock(ClassifiedHistoryProjection.class);
    final List<ClassifiedHistoryProjection> projections = List.of(projection1, projection2);

    final UUID id1 = UUID.randomUUID();
    final UUID classifiedId1 = UUID.randomUUID();
    final ClassifiedStatus previousStatus1 = Instancio.create(ClassifiedStatus.class);
    final ClassifiedStatus newStatus1 = Instancio.create(ClassifiedStatus.class);
    final Instant changedAt1 = Instancio.create(Instant.class);

    final UUID id2 = UUID.randomUUID();
    final UUID classifiedId2 = UUID.randomUUID();
    final ClassifiedStatus previousStatus2 = Instancio.create(ClassifiedStatus.class);
    final ClassifiedStatus newStatus2 = Instancio.create(ClassifiedStatus.class);
    final Instant changedAt2 = Instancio.create(Instant.class);

    when(projection1.getId()).thenReturn(id1);
    when(projection1.getClassifiedId()).thenReturn(classifiedId1);
    when(projection1.getPreviousStatus()).thenReturn(previousStatus1);
    when(projection1.getNewStatus()).thenReturn(newStatus1);
    when(projection1.getChangedAt()).thenReturn(changedAt1);

    when(projection2.getId()).thenReturn(id2);
    when(projection2.getClassifiedId()).thenReturn(classifiedId2);
    when(projection2.getPreviousStatus()).thenReturn(previousStatus2);
    when(projection2.getNewStatus()).thenReturn(newStatus2);
    when(projection2.getChangedAt()).thenReturn(changedAt2);

    // WHEN

    final List<ClassifiedHistoryDto> historyDtos = mapper.map(projections);

    // THEN
    assertThat(historyDtos)
        .hasSize(2)
        .satisfiesExactlyInAnyOrder(
            historyDto -> {
              assertThat(historyDto.getId()).isEqualTo(id1);
              assertThat(historyDto.getClassifiedId()).isEqualTo(classifiedId1);
              assertThat(historyDto.getPreviousStatus()).isEqualTo(previousStatus1);
              assertThat(historyDto.getNewStatus()).isEqualTo(newStatus1);
              assertThat(historyDto.getChangedAt()).isEqualTo(changedAt1);
            },
            historyDto -> {
              assertThat(historyDto.getId()).isEqualTo(id2);
              assertThat(historyDto.getClassifiedId()).isEqualTo(classifiedId2);
              assertThat(historyDto.getPreviousStatus()).isEqualTo(previousStatus2);
              assertThat(historyDto.getNewStatus()).isEqualTo(newStatus2);
              assertThat(historyDto.getChangedAt()).isEqualTo(changedAt2);
            });
  }
}
