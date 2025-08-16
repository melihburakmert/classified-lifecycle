package mbm.classified_lifecycle.classified.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedEntity;
import mbm.classified_lifecycle.classified.persistence.projection.ClassifiedStatusProjection;
import mbm.classified_lifecycle.common.ClassifiedCategory;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifiedRepository extends JpaRepository<ClassifiedEntity, UUID> {

  boolean existsByTitleAndDescriptionAndCategory(
      String title, String description, ClassifiedCategory category);

  @Modifying
  @Query("UPDATE ClassifiedEntity c " + "SET c.status = :status " + "WHERE c.id IN :ids")
  int updateStatusForIds(@Param("ids") List<UUID> ids, @Param("status") ClassifiedStatus status);

  @Query(
      "SELECT c.id as id, c.status as status "
          + "FROM ClassifiedEntity c "
          + "WHERE c.expiresAt <= :now AND c.status NOT IN :excludedStatuses")
  Page<ClassifiedStatusProjection> findExpiredClassifiedsExcludedStatuses(
      @Param("now") Instant now,
      @Param("excludedStatuses") List<ClassifiedStatus> excludedStatuses,
      Pageable pageable);
}
