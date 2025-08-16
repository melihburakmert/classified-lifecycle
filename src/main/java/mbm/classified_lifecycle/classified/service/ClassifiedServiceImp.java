package mbm.classified_lifecycle.classified.service;

import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mbm.classified_lifecycle.classified.ClassifiedDto;
import mbm.classified_lifecycle.classified.ClassifiedRequestDto;
import mbm.classified_lifecycle.classified.ClassifiedService;
import mbm.classified_lifecycle.classified.exception.InvalidActivateException;
import mbm.classified_lifecycle.classified.exception.InvalidDeactivateException;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedEntity;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedHistoryEntity;
import mbm.classified_lifecycle.classified.persistence.mapper.ClassifiedEntityMapper;
import mbm.classified_lifecycle.classified.persistence.repository.ClassifiedRepository;
import mbm.classified_lifecycle.common.ClassifiedCategory;
import mbm.classified_lifecycle.common.ClassifiedNotFoundException;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassifiedServiceImp implements ClassifiedService {

  private final ClassifiedRepository classifiedRepository;
  private final ClassifiedEntityMapper classifiedEntityToDtoMapper;
  private final ClassifiedExpirationCalculator classifiedExpirationCalculator;

  @Override
  @Transactional
  public ClassifiedDto createClassified(final ClassifiedRequestDto requestDto) {
    log.info("Creating classified with title: {}", requestDto.title());

    final ClassifiedCategory category = requestDto.category();
    final String title = requestDto.title();
    final String description = requestDto.description();

    final ClassifiedStatus initialStatus = determineInitialStatus(title, description, category);
    final Instant createdAt = Instant.now();
    final Instant expiresAt =
        classifiedExpirationCalculator.calculateExpirationDate(category, createdAt);

    final ClassifiedEntity classifiedEntity =
        ClassifiedEntity.builder()
            .title(title)
            .description(description)
            .category(category)
            .status(initialStatus)
            .createdAt(createdAt)
            .expiresAt(expiresAt)
            .build();

    final ClassifiedEntity savedEntity = classifiedRepository.save(classifiedEntity);
    createInitialStatusHistory(savedEntity);

    return classifiedEntityToDtoMapper.map(savedEntity);
  }

  @Override
  @Transactional
  public ClassifiedDto activateClassified(final UUID id) {
    log.info("Activating classified with ID: {}", id);

    final ClassifiedEntity classifiedEntity = findClassifiedById(id);
    final ClassifiedStatus currentStatus = classifiedEntity.getStatus();

    if (currentStatus != ClassifiedStatus.PENDING_APPROVAL) {
      throw new InvalidActivateException(id, currentStatus);
    }

    classifiedEntity.setStatus(ClassifiedStatus.ACTIVE);

    createStatusHistoryEntry(classifiedEntity, currentStatus, ClassifiedStatus.ACTIVE);

    final ClassifiedEntity updatedEntity = classifiedRepository.save(classifiedEntity);
    log.info("Classified with ID: {} has been activated", id);

    return classifiedEntityToDtoMapper.map(updatedEntity);
  }

  @Override
  @Transactional
  public ClassifiedDto deactivateClassified(final UUID id) {
    log.info("Deactivating classified with ID: {}", id);

    final ClassifiedEntity classifiedEntity = findClassifiedById(id);
    final ClassifiedStatus currentStatus = classifiedEntity.getStatus();

    if ((currentStatus != ClassifiedStatus.ACTIVE
        && currentStatus != ClassifiedStatus.PENDING_APPROVAL)) {
      throw new InvalidDeactivateException(id, currentStatus);
    }

    classifiedEntity.setStatus(ClassifiedStatus.INACTIVE);

    createStatusHistoryEntry(classifiedEntity, currentStatus, ClassifiedStatus.INACTIVE);

    final ClassifiedEntity updatedEntity = classifiedRepository.save(classifiedEntity);
    log.info("Classified with ID: {} has been deactivated", id);

    return classifiedEntityToDtoMapper.map(updatedEntity);
  }

  private void createInitialStatusHistory(final ClassifiedEntity entity) {
    createStatusHistoryEntry(entity, null, entity.getStatus());
  }

  private void createStatusHistoryEntry(
      final ClassifiedEntity entity,
      final ClassifiedStatus previousStatus,
      final ClassifiedStatus newStatus) {

    final ClassifiedHistoryEntity historyEntry =
        ClassifiedHistoryEntity.builder()
            .classified(entity)
            .previousStatus(previousStatus)
            .newStatus(newStatus)
            .changedAt(Instant.now())
            .build();

    entity.getStatusHistory().add(historyEntry);
  }

  private ClassifiedStatus determineInitialStatus(
      final String title, final String description, final ClassifiedCategory category) {
    final boolean isDuplicate =
        classifiedRepository.existsByTitleAndDescriptionAndCategory(title, description, category);

    return isDuplicate
        ? ClassifiedStatus.DUPLICATE
        : (category == ClassifiedCategory.SHOPPING
            ? ClassifiedStatus.ACTIVE
            : ClassifiedStatus.PENDING_APPROVAL);
  }

  private ClassifiedEntity findClassifiedById(final UUID id) {
    return classifiedRepository.findById(id).orElseThrow(() -> new ClassifiedNotFoundException(id));
  }
}
