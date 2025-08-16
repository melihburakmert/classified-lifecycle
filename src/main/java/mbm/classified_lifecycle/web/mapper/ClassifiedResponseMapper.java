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
        .id(classifiedDto.id())
        .title(classifiedDto.title())
        .description(classifiedDto.description())
        .category(mapCategory(classifiedDto))
        .status(mapStatus(classifiedDto))
        .createdAt(classifiedDto.createdAt())
        .expiresAt(classifiedDto.expiresAt());
  }

  private ClassifiedCategory mapCategory(final ClassifiedDto classifiedDto) {
    return ClassifiedCategory.fromValue(classifiedDto.category().getValue());
  }

  private ClassifiedStatus mapStatus(final ClassifiedDto classifiedDto) {
    return ClassifiedStatus.fromValue(classifiedDto.status().getValue());
  }
}
