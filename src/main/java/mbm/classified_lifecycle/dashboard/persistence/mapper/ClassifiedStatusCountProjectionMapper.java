package mbm.classified_lifecycle.dashboard.persistence.mapper;

import java.util.List;
import java.util.Map;
import mbm.classified_lifecycle.dashboard.ClassifiedStatisticsDto;
import mbm.classified_lifecycle.dashboard.persistence.projection.ClassifiedStatusCountProjection;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedStatusCountProjectionMapper {

  public ClassifiedStatisticsDto map(final List<ClassifiedStatusCountProjection> projections) {
    final Map<String, Integer> countByStatus =
        projections.stream()
            .collect(
                java.util.stream.Collectors.toMap(
                    ClassifiedStatusCountProjection::getStatus,
                    ClassifiedStatusCountProjection::getCount));

    return ClassifiedStatisticsDto.builder().countByStatus(countByStatus).build();
  }
}
