package mbm.classified_lifecycle.classified.scheduled;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import mbm.classified_lifecycle.classified.persistence.projection.ClassifiedStatusProjection;
import mbm.classified_lifecycle.classified.persistence.repository.ClassifiedRepository;
import mbm.classified_lifecycle.classified.scheduled.config.ExpirationServiceProperties;
import mbm.classified_lifecycle.classified.scheduled.service.ClassifiedExpirationBatchProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ClassifiedExpirationServiceUT {

  @Mock private ClassifiedRepository classifiedRepository;
  @Mock private ClassifiedExpirationBatchProcessor batchProcessor;
  @Mock private TaskExecutor expirationTaskExecutor;
  @Mock private ExpirationServiceProperties expirationServiceProperties;

  private ClassifiedExpirationService classifiedExpirationService;

  @BeforeEach
  void setUp() {
    classifiedExpirationService =
        new ClassifiedExpirationService(
            classifiedRepository,
            batchProcessor,
            expirationTaskExecutor,
            expirationServiceProperties);
  }

  @Test
  void test_expireClassifieds_singlePage() {
    // GIVEN
    when(expirationServiceProperties.getPageSize()).thenReturn(10);
    final List<ClassifiedStatusProjection> batch =
        List.of(mock(ClassifiedStatusProjection.class), mock(ClassifiedStatusProjection.class));
    final Page<ClassifiedStatusProjection> page =
        new PageImpl<>(batch, PageRequest.of(0, 10), batch.size());
    when(classifiedRepository.findExpiredClassifiedsExcludedStatuses(
            any(), anyList(), any(PageRequest.class)))
        .thenReturn(page);

    // WHEN
    classifiedExpirationService.expireClassifieds();

    // THEN
    verify(classifiedRepository)
        .findExpiredClassifiedsExcludedStatuses(any(), anyList(), any(PageRequest.class));
    verify(expirationTaskExecutor).execute(any(Runnable.class));

    final ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(expirationTaskExecutor).execute(runnableCaptor.capture());
    runnableCaptor.getValue().run();
    verify(batchProcessor).processBatch(batch);
  }

  @Test
  void test_expireClassifieds_multiplePages() {
    // GIVEN
    when(expirationServiceProperties.getPageSize()).thenReturn(2);

    final List<ClassifiedStatusProjection> batch1 =
        List.of(mock(ClassifiedStatusProjection.class), mock(ClassifiedStatusProjection.class));
    final List<ClassifiedStatusProjection> batch2 = List.of(mock(ClassifiedStatusProjection.class));
    final Page<ClassifiedStatusProjection> page1 = new PageImpl<>(batch1, PageRequest.of(0, 2), 3);
    final Page<ClassifiedStatusProjection> page2 = new PageImpl<>(batch2, PageRequest.of(1, 2), 3);

    when(classifiedRepository.findExpiredClassifiedsExcludedStatuses(
            any(), anyList(), eq(PageRequest.of(0, 2))))
        .thenReturn(page1);
    when(classifiedRepository.findExpiredClassifiedsExcludedStatuses(
            any(), anyList(), eq(PageRequest.of(1, 2))))
        .thenReturn(page2);

    // WHEN
    classifiedExpirationService.expireClassifieds();

    // THEN
    verify(classifiedRepository)
        .findExpiredClassifiedsExcludedStatuses(any(), anyList(), eq(PageRequest.of(0, 2)));
    verify(classifiedRepository)
        .findExpiredClassifiedsExcludedStatuses(any(), anyList(), eq(PageRequest.of(1, 2)));
    verify(expirationTaskExecutor, times(2)).execute(any(Runnable.class));
  }

  @Test
  void test_expireClassifieds_emptyPage() {
    // GIVEN
    when(expirationServiceProperties.getPageSize()).thenReturn(10);
    final Page<ClassifiedStatusProjection> emptyPage =
        new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
    when(classifiedRepository.findExpiredClassifiedsExcludedStatuses(
            any(), anyList(), any(PageRequest.class)))
        .thenReturn(emptyPage);

    // WHEN
    classifiedExpirationService.expireClassifieds();

    // THEN
    verify(classifiedRepository)
        .findExpiredClassifiedsExcludedStatuses(any(), anyList(), any(PageRequest.class));
    verify(expirationTaskExecutor).execute(any(Runnable.class));
  }
}
