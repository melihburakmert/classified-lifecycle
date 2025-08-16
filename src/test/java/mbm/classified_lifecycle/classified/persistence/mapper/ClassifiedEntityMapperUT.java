package mbm.classified_lifecycle.classified.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import mbm.classified_lifecycle.classified.ClassifiedDto;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassifiedEntityMapperUT {

  private ClassifiedEntityMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ClassifiedEntityMapper();
  }

  @Test
  void test_map() {
    // GIVEN
    final ClassifiedEntity entity = Instancio.create(ClassifiedEntity.class);

    // WHEN
    final ClassifiedDto dto = mapper.map(entity);

    // THEN
    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(entity.getId());
    assertThat(dto.getTitle()).isEqualTo(entity.getTitle());
    assertThat(dto.getDescription()).isEqualTo(entity.getDescription());
    assertThat(dto.getCategory()).isEqualTo(entity.getCategory());
    assertThat(dto.getStatus()).isEqualTo(entity.getStatus());
    assertThat(dto.getCreatedAt()).isEqualTo(entity.getCreatedAt());
    assertThat(dto.getExpiresAt()).isEqualTo(entity.getExpiresAt());
  }
}
