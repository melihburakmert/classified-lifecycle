package mbm.classified_lifecycle.dashboard.persistence.entity;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "classified_history")
public class ClassifiedHistoryReadOnlyEntity {

  @Id private UUID id;
}
