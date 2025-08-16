package mbm.classified_lifecycle.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.create;

import mbm.classified_lifecycle.dashboard.ClassifiedStatisticsDto;
import mbm.classified_lifecycle.web.model.ClassifiedStatisticsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassifiedStatisticsResponseMapperUT {

  private ClassifiedStatisticsResponseMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ClassifiedStatisticsResponseMapper();
  }

  @Test
  void test_map() {
    // GIVEN
    final ClassifiedStatisticsDto dto = create(ClassifiedStatisticsDto.class);

    // WHEN
    final ClassifiedStatisticsResponse response = mapper.map(dto);

    // THEN
    assertThat(response).isNotNull();
    assertThat(response.getCountByStatus()).isEqualTo(dto.countByStatus());
  }
}
