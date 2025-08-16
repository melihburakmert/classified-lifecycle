package mbm.classified_lifecycle.classified.persistence.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mbm.classified_lifecycle.common.ClassifiedCategory;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "classified")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassifiedEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Type(type = "uuid-char")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "title", nullable = false, length = 50)
  private String title;

  @Column(name = "description", nullable = false, length = 200)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false, length = 20)
  private ClassifiedCategory category;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private ClassifiedStatus status;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  @OneToMany(mappedBy = "classified", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ClassifiedHistoryEntity> statusHistory = new ArrayList<>();
}
