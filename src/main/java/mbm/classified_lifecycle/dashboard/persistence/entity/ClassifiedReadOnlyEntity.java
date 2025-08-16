package mbm.classified_lifecycle.dashboard.persistence.entity;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "classified")
public class ClassifiedReadOnlyEntity {

  @Id private UUID id;
}
