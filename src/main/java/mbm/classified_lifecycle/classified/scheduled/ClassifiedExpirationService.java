package mbm.classified_lifecycle.classified.scheduled;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mbm.classified_lifecycle.classified.persistence.projection.ClassifiedStatusProjection;
import mbm.classified_lifecycle.classified.persistence.repository.ClassifiedRepository;
import mbm.classified_lifecycle.classified.scheduled.config.ExpirationServiceProperties;
import mbm.classified_lifecycle.classified.scheduled.service.ClassifiedExpirationBatchProcessor;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassifiedExpirationService {

  private static final List<ClassifiedStatus> EXCLUDED_STATUSES =
      List.of(ClassifiedStatus.EXPIRED, ClassifiedStatus.DUPLICATE);

  private final ClassifiedRepository classifiedRepository;
  private final ClassifiedExpirationBatchProcessor batchProcessor;
  private final TaskExecutor expirationTaskExecutor;
  private final ExpirationServiceProperties expirationServiceProperties;

  @Transactional(readOnly = true)
  public void expireClassifieds() {
    log.info("Starting expiration check process");
    int page = 0;
    final int pageSize = expirationServiceProperties.getPageSize();
    Page<ClassifiedStatusProjection> expiredClassifieds;

    do {
      expiredClassifieds =
          classifiedRepository.findExpiredClassifiedsExcludedStatuses(
              Instant.now(), EXCLUDED_STATUSES, PageRequest.of(page, pageSize));

      final List<ClassifiedStatusProjection> currentBatch = expiredClassifieds.getContent();
      expirationTaskExecutor.execute(() -> batchProcessor.processBatch(currentBatch));
      page++;

    } while (expiredClassifieds.hasNext());

    log.info("Scheduled all expiration batches for processing");
  }
}
