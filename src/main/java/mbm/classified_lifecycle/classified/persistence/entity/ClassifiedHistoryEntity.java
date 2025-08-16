package mbm.classified_lifecycle.classified.persistence.entity;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "classified_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassifiedHistoryEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Type(type = "uuid-char")
  @Column(name = "id", updatable = false, nullable = false)
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
