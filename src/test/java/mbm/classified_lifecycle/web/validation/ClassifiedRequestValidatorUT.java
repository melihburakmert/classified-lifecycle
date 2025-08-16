package mbm.classified_lifecycle.web.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import mbm.classified_lifecycle.web.model.ClassifiedRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassifiedRequestValidatorUT {

  @Mock private BadWordsValidator badWordsValidator;
  @Mock private ConstraintValidatorContext context;
  @Mock private ConstraintValidatorContext.ConstraintViolationBuilder builder;

  @Mock
  private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext
      nodeBuilder;

  private ClassifiedRequestValidator validator;

  @BeforeEach
  void setUp() {
    validator = new ClassifiedRequestValidator(badWordsValidator);
  }

  @Test
  void test_isValid_returnsTrue_whenRequestIsNull() {
    // GIVEN / WHEN
    final boolean result = validator.isValid(null, context);

    // THEN
    assertThat(result).isTrue();
    verifyNoInteractions(badWordsValidator);
  }

  @Test
  void test_isValid_returnsTrue_whenTitleIsNull() {
    // GIVEN
    final ClassifiedRequest request = new ClassifiedRequest();
    request.setTitle(null);

    // WHEN
    final boolean result = validator.isValid(request, context);

    // THEN
    assertThat(result).isTrue();
    verifyNoInteractions(badWordsValidator);
  }

  @Test
  void test_isValid_returnsFalse_whenTitleContainsBadWords() {
    // GIVEN
    final String title = Instancio.create(String.class);
    final ClassifiedRequest request = new ClassifiedRequest();
    request.setTitle(title);

    when(badWordsValidator.containsBadWords(title)).thenReturn(true);
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    when(builder.addPropertyNode("title")).thenReturn(nodeBuilder);
    when(nodeBuilder.addConstraintViolation()).thenReturn(context);

    // WHEN
    final boolean result = validator.isValid(request, context);

    // THEN
    assertThat(result).isFalse();
    verify(context).disableDefaultConstraintViolation();
    verify(context).buildConstraintViolationWithTemplate("contains forbidden words");
    verify(builder).addPropertyNode("title");
    verify(nodeBuilder).addConstraintViolation();
  }

  @Test
  void test_isValid_returnsTrue_whenTitleDoesNotContainBadWords() {
    // GIVEN
    final String title = Instancio.create(String.class);
    final ClassifiedRequest request = new ClassifiedRequest();

    request.setTitle(title);
    when(badWordsValidator.containsBadWords(title)).thenReturn(false);

    // WHEN
    final boolean result = validator.isValid(request, context);

    // THEN
    assertThat(result).isTrue();
    verify(badWordsValidator).containsBadWords(title);
    verifyNoMoreInteractions(context);
  }
}
