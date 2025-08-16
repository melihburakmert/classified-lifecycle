package mbm.classified_lifecycle.classified.scheduled.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedHistoryEntity;
import mbm.classified_lifecycle.classified.persistence.projection.ClassifiedStatusProjection;
import mbm.classified_lifecycle.classified.persistence.repository.ClassifiedHistoryRepository;
import mbm.classified_lifecycle.classified.persistence.repository.ClassifiedRepository;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassifiedExpirationBatchProcessorUT {

  @Mock private ClassifiedRepository classifiedRepository;
  @Mock private ClassifiedHistoryRepository classifiedHistoryRepository;

  private ClassifiedExpirationBatchProcessor processor;

  @BeforeEach
  void setUp() {
    processor =
        new ClassifiedExpirationBatchProcessor(classifiedRepository, classifiedHistoryRepository);
  }

  @Test
  @SuppressWarnings("unchecked")
  void test_processBatch() {
    // GIVEN
    final UUID id1 = UUID.randomUUID();
    final UUID id2 = UUID.randomUUID();

    final ClassifiedStatusProjection projection1 = mock(ClassifiedStatusProjection.class);
    final ClassifiedStatusProjection projection2 = mock(ClassifiedStatusProjection.class);

    when(projection1.getId()).thenReturn(id1);
    when(projection1.getStatus()).thenReturn(ClassifiedStatus.ACTIVE);
    when(projection2.getId()).thenReturn(id2);
    when(projection2.getStatus()).thenReturn(ClassifiedStatus.PENDING_APPROVAL);

    final List<ClassifiedStatusProjection> batch = List.of(projection1, projection2);

    when(classifiedRepository.updateStatusForIds(anyList(), eq(ClassifiedStatus.EXPIRED)))
        .thenReturn(batch.size());

    // WHEN
    processor.processBatch(batch);

    // THEN
    verify(classifiedRepository).updateStatusForIds(List.of(id1, id2), ClassifiedStatus.EXPIRED);

    final ArgumentCaptor<List<ClassifiedHistoryEntity>> captor =
        ArgumentCaptor.forClass(List.class);
    verify(classifiedHistoryRepository).saveAll(captor.capture());
    final List<ClassifiedHistoryEntity> savedHistory = captor.getValue();

    assertThat(savedHistory)
        .hasSize(2)
        .anySatisfy(
            history -> {
              assertThat(history.getClassified().getId()).isIn(id1, id2);
              assertThat(history.getPreviousStatus())
                  .isIn(ClassifiedStatus.ACTIVE, ClassifiedStatus.PENDING_APPROVAL);
              assertThat(history.getNewStatus()).isEqualTo(ClassifiedStatus.EXPIRED);
              assertThat(history.getChangedAt()).isNotNull();
            });
  }

  @Test
  void test_processBatch_emptyBatch() {
    // GIVEN
    final List<ClassifiedStatusProjection> batch = List.of();

    // WHEN
    processor.processBatch(batch);

    // THEN
    verifyNoInteractions(classifiedRepository, classifiedHistoryRepository);
  }
}
