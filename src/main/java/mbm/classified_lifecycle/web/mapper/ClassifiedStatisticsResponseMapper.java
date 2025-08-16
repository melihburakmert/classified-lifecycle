package mbm.classified_lifecycle.web.mapper;

import mbm.classified_lifecycle.dashboard.ClassifiedStatisticsDto;
import mbm.classified_lifecycle.web.model.ClassifiedStatisticsResponse;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedStatisticsResponseMapper {

  public ClassifiedStatisticsResponse map(final ClassifiedStatisticsDto dto) {
    return new ClassifiedStatisticsResponse().countByStatus(dto.countByStatus());
  }
}
