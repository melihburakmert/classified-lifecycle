package mbm.classified_lifecycle.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import mbm.classified_lifecycle.web.model.ClassifiedRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassifiedRequestValidator
    implements ConstraintValidator<ValidClassifiedRequest, ClassifiedRequest> {

  private final BadWordsValidator badWordsValidator;

  @Override
  public void initialize(final ValidClassifiedRequest constraintAnnotation) {
    // No initialization needed for this validator
  }

  @Override
  public boolean isValid(
      final ClassifiedRequest request, final ConstraintValidatorContext context) {
    if (request == null || request.getTitle() == null) {
      return true; // Let other validations handle null values
    }

    if (badWordsValidator.containsBadWords(request.getTitle())) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate("contains forbidden words")
          .addPropertyNode("title")
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}
