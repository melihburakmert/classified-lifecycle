package mbm.classified_lifecycle.dashboard.persistence.repository;

import java.util.List;
import java.util.UUID;
import mbm.classified_lifecycle.dashboard.persistence.entity.ClassifiedHistoryReadOnlyEntity;
import mbm.classified_lifecycle.dashboard.persistence.projection.ClassifiedHistoryProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClassifiedHistoryReadOnlyRepository
    extends ReadOnlyRepository<ClassifiedHistoryReadOnlyEntity, UUID> {

  @Query(
      "SELECT h.id as id, h.classified.id as classifiedId, h.previousStatus as previousStatus, "
          + "h.newStatus as newStatus, h.changedAt as changedAt "
          + "FROM ClassifiedHistoryEntity h "
          + "WHERE h.classified.id = :classifiedId "
          + "ORDER BY h.changedAt DESC")
  List<ClassifiedHistoryProjection> findByClassifiedIdOrderByChangedAtDesc(
      @Param("classifiedId") UUID classifiedId);
}
