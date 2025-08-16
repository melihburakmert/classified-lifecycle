package mbm.classified_lifecycle.dashboard.persistence.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepository<Entity, ID> extends Repository<Entity, ID> {
  boolean existsById(ID id);
}
