package mbm.classified_lifecycle.classified;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import mbm.classified_lifecycle.common.ClassifiedCategory;

@Value
@Builder
public class ClassifiedRequestDto {
  @NotBlank
  @Size(min = 10, max = 50)
  @Pattern(regexp = "^[a-zA-Z0-9].*")
  String title;

  @NotBlank
  @Size(min = 20, max = 200)
  String description;

  @NotNull ClassifiedCategory category;
}
