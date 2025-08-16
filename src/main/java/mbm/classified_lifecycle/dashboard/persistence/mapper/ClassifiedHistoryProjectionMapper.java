package mbm.classified_lifecycle.dashboard.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;
import mbm.classified_lifecycle.dashboard.ClassifiedHistoryDto;
import mbm.classified_lifecycle.dashboard.persistence.projection.ClassifiedHistoryProjection;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedHistoryProjectionMapper {

  public List<ClassifiedHistoryDto> map(final List<ClassifiedHistoryProjection> projections) {
    return projections.stream().map(this::map).collect(Collectors.toList());
  }

  private ClassifiedHistoryDto map(final ClassifiedHistoryProjection projection) {
    return ClassifiedHistoryDto.builder()
        .id(projection.getId())
        .classifiedId(projection.getClassifiedId())
        .previousStatus(projection.getPreviousStatus())
        .newStatus(projection.getNewStatus())
        .changedAt(projection.getChangedAt())
        .build();
  }
}
