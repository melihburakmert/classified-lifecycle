package mbm.classified_lifecycle.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.create;

import mbm.classified_lifecycle.classified.ClassifiedDto;
import mbm.classified_lifecycle.web.model.ClassifiedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassifiedResponseMapperUT {

  private ClassifiedResponseMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ClassifiedResponseMapper();
  }

  @Test
  void test_map() {
    // GIVEN
    final ClassifiedDto dto = create(ClassifiedDto.class);

    // WHEN
    final ClassifiedResponse response = mapper.map(dto);

    // THEN
    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(dto.id());
    assertThat(response.getTitle()).isEqualTo(dto.title());
    assertThat(response.getDescription()).isEqualTo(dto.description());
    assertThat(response.getCategory()).hasToString(dto.category().getValue());
    assertThat(response.getStatus()).hasToString(dto.status().getValue());
    assertThat(response.getCreatedAt()).isEqualTo(dto.createdAt());
    assertThat(response.getExpiresAt()).isEqualTo(dto.expiresAt());
  }
}
