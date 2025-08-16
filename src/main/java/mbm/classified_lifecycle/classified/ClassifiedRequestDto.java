package mbm.classified_lifecycle.classified;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
