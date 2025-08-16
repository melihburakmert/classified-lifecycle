package mbm.classified_lifecycle.classified.scheduled.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedEntity;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedHistoryEntity;
import mbm.classified_lifecycle.classified.persistence.projection.ClassifiedStatusProjection;
import mbm.classified_lifecycle.classified.persistence.repository.ClassifiedHistoryRepository;
import mbm.classified_lifecycle.classified.persistence.repository.ClassifiedRepository;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassifiedExpirationBatchProcessor {

  private final ClassifiedRepository classifiedRepository;
  private final ClassifiedHistoryRepository classifiedHistoryRepository;

  @Transactional
  public void processBatch(final List<ClassifiedStatusProjection> batch) {
    if (batch.isEmpty()) return;

    final List<UUID> ids = new ArrayList<>(batch.size());
    final List<ClassifiedHistoryEntity> historyEntries = new ArrayList<>(batch.size());

    batch.forEach(
        projection -> {
          ids.add(projection.getId());
          final ClassifiedHistoryEntity historyEntry = buildExpiredHistoryEntry(projection);
          historyEntries.add(historyEntry);
        });

    final int updateCount = classifiedRepository.updateStatusForIds(ids, ClassifiedStatus.EXPIRED);
    classifiedHistoryRepository.saveAll(historyEntries);

    log.info("Processed batch of {} expired classifieds", updateCount);
  }

  private ClassifiedHistoryEntity buildExpiredHistoryEntry(
      final ClassifiedStatusProjection projection) {
    return ClassifiedHistoryEntity.builder()
        .classified(ClassifiedEntity.builder().id(projection.getId()).build())
        .previousStatus(projection.getStatus())
        .newStatus(ClassifiedStatus.EXPIRED)
        .changedAt(Instant.now())
        .build();
  }
}
