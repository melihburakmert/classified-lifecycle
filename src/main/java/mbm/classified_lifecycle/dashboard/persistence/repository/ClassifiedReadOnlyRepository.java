package mbm.classified_lifecycle.dashboard.persistence.repository;

import java.util.List;
import java.util.UUID;
import mbm.classified_lifecycle.dashboard.persistence.entity.ClassifiedReadOnlyEntity;
import mbm.classified_lifecycle.dashboard.persistence.projection.ClassifiedStatusCountProjection;
import org.springframework.data.jpa.repository.Query;

public interface ClassifiedReadOnlyRepository
    extends ReadOnlyRepository<ClassifiedReadOnlyEntity, UUID> {

  @Query(
      "SELECT c.status as status, COUNT(c.id) as count "
          + "FROM ClassifiedEntity c "
          + "GROUP BY c.status")
  List<ClassifiedStatusCountProjection> getClassifiedStatusGroupByCount();
}
