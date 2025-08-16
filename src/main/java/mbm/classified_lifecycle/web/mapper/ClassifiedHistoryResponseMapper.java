package mbm.classified_lifecycle.web.mapper;

import java.util.List;
import java.util.stream.Collectors;
import mbm.classified_lifecycle.dashboard.ClassifiedHistoryDto;
import mbm.classified_lifecycle.web.model.ClassifiedStatus;
import mbm.classified_lifecycle.web.model.ClassifiedStatusHistoryResponse;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedHistoryResponseMapper {

  public List<ClassifiedStatusHistoryResponse> mapToList(final List<ClassifiedHistoryDto> dtos) {
    return dtos.stream().map(this::map).collect(Collectors.toList());
  }

  private ClassifiedStatusHistoryResponse map(final ClassifiedHistoryDto dto) {
    final ClassifiedStatus previousStatus =
        dto.getPreviousStatus() != null ? getClassifiedStatus(dto.getPreviousStatus()) : null;
    final ClassifiedStatus newStatus = getClassifiedStatus(dto.getNewStatus());

    return new ClassifiedStatusHistoryResponse()
        .id(dto.getId())
        .classifiedId(dto.getClassifiedId())
        .previousStatus(previousStatus)
        .newStatus(newStatus)
        .changedAt(dto.getChangedAt());
  }

  private ClassifiedStatus getClassifiedStatus(
      final mbm.classified_lifecycle.common.ClassifiedStatus dto) {
    return ClassifiedStatus.fromValue(dto.getValue());
  }
}
