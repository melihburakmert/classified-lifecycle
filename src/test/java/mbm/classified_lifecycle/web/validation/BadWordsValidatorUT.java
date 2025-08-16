package mbm.classified_lifecycle.web.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BadWordsValidatorUT {

  private BadWordsValidator validator;

  @BeforeEach
  void setUp() throws Exception {
    validator = new BadWordsValidator();

    final Field badWordsField = BadWordsValidator.class.getDeclaredField("badWords");
    badWordsField.setAccessible(true);
    badWordsField.set(validator, Set.of("serdar", "beye", "opsiyon"));
  }

  @Test
  void test_containsBadWords_true() {
    // GIVEN
    final String title = "Serdar beye opsiyonlanmistir";

    // WHEN
    final boolean result = validator.containsBadWords(title);

    // THEN
    assertThat(result).isTrue();
  }

  @Test
  void test_containsBadWords_false() {
    // GIVEN
    final String title = "Garaj arabası, sadece hafta sonları gezmek için kullanıldı";

    // WHEN
    final boolean result = validator.containsBadWords(title);

    // THEN
    assertThat(result).isFalse();
  }

  @Test
  void test_containsBadWords_false_titleIsNullOrEmpty() {
    // GIVEN / WHEN / THEN
    assertThat(validator.containsBadWords(null)).isFalse();
    assertThat(validator.containsBadWords("")).isFalse();
  }

  @Test
  void test_containsBadWords_false_badWordsSetIsEmpty() throws Exception {
    // GIVEN
    final Field badWordsField = BadWordsValidator.class.getDeclaredField("badWords");
    badWordsField.setAccessible(true);
    badWordsField.set(validator, Set.of());
    final String title = "bon jovi'den citir hasarli";

    // WHEN
    final boolean result = validator.containsBadWords(title);

    // THEN
    assertThat(result).isFalse();
  }
}
