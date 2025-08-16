package mbm.classified_lifecycle.web.mapper;

import mbm.classified_lifecycle.classified.ClassifiedDto;
import mbm.classified_lifecycle.web.model.ClassifiedCategory;
import mbm.classified_lifecycle.web.model.ClassifiedResponse;
import mbm.classified_lifecycle.web.model.ClassifiedStatus;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedResponseMapper {

  public ClassifiedResponse map(final ClassifiedDto classifiedDto) {
    return new ClassifiedResponse()
        .id(classifiedDto.getId())
        .title(classifiedDto.getTitle())
        .description(classifiedDto.getDescription())
        .category(mapCategory(classifiedDto))
        .status(mapStatus(classifiedDto))
        .createdAt(classifiedDto.getCreatedAt())
        .expiresAt(classifiedDto.getExpiresAt());
  }

  private ClassifiedCategory mapCategory(final ClassifiedDto classifiedDto) {
    return ClassifiedCategory.fromValue(classifiedDto.getCategory().getValue());
  }

  private ClassifiedStatus mapStatus(final ClassifiedDto classifiedDto) {
    return ClassifiedStatus.fromValue(classifiedDto.getStatus().getValue());
  }
}
