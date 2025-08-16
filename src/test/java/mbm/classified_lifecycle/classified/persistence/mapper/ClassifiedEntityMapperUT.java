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
    assertThat(dto.id()).isEqualTo(entity.getId());
    assertThat(dto.title()).isEqualTo(entity.getTitle());
    assertThat(dto.description()).isEqualTo(entity.getDescription());
    assertThat(dto.category()).isEqualTo(entity.getCategory());
    assertThat(dto.status()).isEqualTo(entity.getStatus());
    assertThat(dto.createdAt()).isEqualTo(entity.getCreatedAt());
    assertThat(dto.expiresAt()).isEqualTo(entity.getExpiresAt());
  }
}
