package mbm.classified_lifecycle.dashboard.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.create;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import mbm.classified_lifecycle.dashboard.ClassifiedStatisticsDto;
import mbm.classified_lifecycle.dashboard.persistence.projection.ClassifiedStatusCountProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassifiedStatusCountProjectionMapperUT {

  private ClassifiedStatusCountProjectionMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ClassifiedStatusCountProjectionMapper();
  }

  @Test
  void test_map() {
    // GIVEN
    final ClassifiedStatusCountProjection projection1 = mock(ClassifiedStatusCountProjection.class);
    final ClassifiedStatusCountProjection projection2 = mock(ClassifiedStatusCountProjection.class);

    final String status1 = ClassifiedStatus.INACTIVE.getValue();
    final Integer count1 = create(Integer.class);

    final String status2 = ClassifiedStatus.ACTIVE.getValue();
    final Integer count2 = create(Integer.class);

    when(projection1.getStatus()).thenReturn(status1);
    when(projection1.getCount()).thenReturn(count1);
    when(projection2.getStatus()).thenReturn(status2);
    when(projection2.getCount()).thenReturn(count2);

    final List<ClassifiedStatusCountProjection> projections = List.of(projection1, projection2);

    // WHEN
    final ClassifiedStatisticsDto dto = mapper.map(projections);

    // THEN
    assertThat(dto).isNotNull();
    assertThat(dto.countByStatus()).isNotNull();
    assertThat(dto.countByStatus()).hasSize(2);
    assertThat(dto.countByStatus()).containsEntry(status1, count1);
    assertThat(dto.countByStatus()).containsEntry(status2, count2);
  }
}
