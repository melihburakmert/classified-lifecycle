package mbm.classified_lifecycle.dashboard.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassifiedDashboardServiceImp implements ClassifiedDashboardService {

  private final ClassifiedReadOnlyRepository classifiedRepository;
  private final ClassifiedHistoryReadOnlyRepository classifiedHistoryRepository;
  private final ClassifiedHistoryProjectionMapper classifiedHistoryEntityMapper;
  private final ClassifiedStatusCountProjectionMapper classifiedStatusCountProjectionMapper;

  @Override
  @Transactional(readOnly = true)
  public List<ClassifiedHistoryDto> getClassifiedStatusHistory(final UUID id) {
    log.info("Getting status history for classified with ID: {}", id);

    if (!classifiedRepository.existsById(id)) {
      throw new ClassifiedNotFoundException(id);
    }

    final List<ClassifiedHistoryProjection> historyEntities =
        classifiedHistoryRepository.findByClassifiedIdOrderByChangedAtDesc(id);

    return classifiedHistoryEntityMapper.map(historyEntities);
  }

  @Override
  @Transactional(readOnly = true)
  public ClassifiedStatisticsDto getClassifiedStatistics() {
    log.info("Retrieving classified ad statistics");

    final List<ClassifiedStatusCountProjection> statistics =
        classifiedRepository.getClassifiedStatusGroupByCount();

    return classifiedStatusCountProjectionMapper.map(statistics);
  }
}
