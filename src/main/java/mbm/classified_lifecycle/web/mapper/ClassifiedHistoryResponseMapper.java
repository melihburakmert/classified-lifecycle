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
        dto.previousStatus() != null ? getClassifiedStatus(dto.previousStatus()) : null;
    final ClassifiedStatus newStatus = getClassifiedStatus(dto.newStatus());

    return new ClassifiedStatusHistoryResponse()
        .id(dto.id())
        .classifiedId(dto.classifiedId())
        .previousStatus(previousStatus)
        .newStatus(newStatus)
        .changedAt(dto.changedAt());
  }

  private ClassifiedStatus getClassifiedStatus(
      final mbm.classified_lifecycle.common.ClassifiedStatus dto) {
    return ClassifiedStatus.fromValue(dto.getValue());
  }
}
