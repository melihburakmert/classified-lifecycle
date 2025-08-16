package mbm.classified_lifecycle.classified.persistence.entity;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "classified_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassifiedHistoryEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "classified_id", nullable = false)
  private ClassifiedEntity classified;

  @Enumerated(EnumType.STRING)
  @Column(name = "previous_status", length = 20)
  private ClassifiedStatus previousStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "new_status", nullable = false, length = 20)
  private ClassifiedStatus newStatus;

  @Column(name = "changed_at", nullable = false)
  private Instant changedAt;
}
