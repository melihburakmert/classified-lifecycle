package mbm.classified_lifecycle.classified.persistence.mapper;

import mbm.classified_lifecycle.classified.ClassifiedDto;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedEntity;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedEntityMapper {

  public ClassifiedDto map(final ClassifiedEntity entity) {
    return ClassifiedDto.builder()
        .id(entity.getId())
        .title(entity.getTitle())
        .description(entity.getDescription())
        .category(entity.getCategory())
        .status(entity.getStatus())
        .createdAt(entity.getCreatedAt())
        .expiresAt(entity.getExpiresAt())
        .build();
  }
}
