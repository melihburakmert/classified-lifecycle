package mbm.classified_lifecycle.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.of;
import static org.instancio.Select.field;

import java.util.List;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import mbm.classified_lifecycle.dashboard.ClassifiedHistoryDto;
import mbm.classified_lifecycle.web.model.ClassifiedStatusHistoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassifiedHistoryResponseMapperUT {

  private ClassifiedHistoryResponseMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ClassifiedHistoryResponseMapper();
  }

  @Test
  void test_mapToList() {
    // GIVEN
    final ClassifiedHistoryDto dto1 =
        of(ClassifiedHistoryDto.class)
            .set(field("previousStatus"), ClassifiedStatus.PENDING_APPROVAL)
            .set(field("newStatus"), ClassifiedStatus.ACTIVE)
            .create();

    final ClassifiedHistoryDto dto2 =
        of(ClassifiedHistoryDto.class)
            .set(field("previousStatus"), null)
            .set(field("newStatus"), ClassifiedStatus.INACTIVE)
            .create();

    final List<ClassifiedHistoryDto> dtos = List.of(dto1, dto2);

    // WHEN
    final List<ClassifiedStatusHistoryResponse> responses = mapper.mapToList(dtos);

    // THEN
    assertThat(responses)
        .hasSize(2)
        .satisfiesExactlyInAnyOrder(
            resp -> {
              assertThat(resp.getId()).isEqualTo(dto1.id());
              assertThat(resp.getClassifiedId()).isEqualTo(dto1.classifiedId());
              assertThat(resp.getPreviousStatus())
                  .hasToString(ClassifiedStatus.PENDING_APPROVAL.getValue());
              assertThat(resp.getNewStatus()).hasToString(ClassifiedStatus.ACTIVE.getValue());
              assertThat(resp.getChangedAt()).isEqualTo(dto1.changedAt());
            },
            resp -> {
              assertThat(resp.getId()).isEqualTo(dto2.id());
              assertThat(resp.getClassifiedId()).isEqualTo(dto2.classifiedId());
              assertThat(resp.getPreviousStatus()).isNull();
              assertThat(resp.getNewStatus()).hasToString(ClassifiedStatus.INACTIVE.getValue());
              assertThat(resp.getChangedAt()).isEqualTo(dto2.changedAt());
            });
  }
}
