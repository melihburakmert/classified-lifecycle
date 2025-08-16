package mbm.classified_lifecycle.web.mapper;

import mbm.classified_lifecycle.classified.ClassifiedRequestDto;
import mbm.classified_lifecycle.common.ClassifiedCategory;
import mbm.classified_lifecycle.web.model.ClassifiedRequest;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedRequestMapper {

  public ClassifiedRequestDto map(final ClassifiedRequest classifiedRequest) {
    return ClassifiedRequestDto.builder()
        .title(classifiedRequest.getTitle())
        .description(classifiedRequest.getDescription())
        .category(mapCategory(classifiedRequest))
        .build();
  }

  private ClassifiedCategory mapCategory(final ClassifiedRequest classifiedRequest) {
    return ClassifiedCategory.fromValue(classifiedRequest.getCategory().getValue());
  }
}
