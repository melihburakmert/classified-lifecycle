package mbm.classified_lifecycle.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.create;

import mbm.classified_lifecycle.classified.ClassifiedRequestDto;
import mbm.classified_lifecycle.web.model.ClassifiedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassifiedRequestMapperUT {

  private ClassifiedRequestMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ClassifiedRequestMapper();
  }

  @Test
  void test_map() {
    // GIVEN
    final ClassifiedRequest request = create(ClassifiedRequest.class);

    // WHEN
    final ClassifiedRequestDto dto = mapper.map(request);

    // THEN
    assertThat(dto.getTitle()).isEqualTo(request.getTitle());
    assertThat(dto.getDescription()).isEqualTo(request.getDescription());
    assertThat(dto.getCategory()).hasToString(request.getCategory().getValue());
  }
}
