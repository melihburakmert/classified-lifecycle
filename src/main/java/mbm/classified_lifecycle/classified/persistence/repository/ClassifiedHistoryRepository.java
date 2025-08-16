package mbm.classified_lifecycle.classified.persistence.repository;

import java.util.UUID;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifiedHistoryRepository extends JpaRepository<ClassifiedHistoryEntity, UUID> {}
