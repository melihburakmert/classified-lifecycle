package mbm.classified_lifecycle.web.validation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BadWordsValidator {

  private Set<String> badWords = new HashSet<>();

  @PostConstruct
  public void init() {
    try {
      final ClassPathResource resource = new ClassPathResource("Badwords.txt");
      if (resource.exists()) {
        try (final BufferedReader reader =
            new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
          badWords = reader.lines().collect(Collectors.toSet());
          log.info("Loaded {} bad words from Badwords.txt", badWords.size());
        }
      } else {
        log.warn("Badwords.txt file not found on classpath");
      }
    } catch (final Exception e) {
      log.error("Error loading bad words file", e);
      badWords = Collections.emptySet();
    }
  }

  public boolean containsBadWords(final String title) {
    if (title == null || title.isEmpty() || badWords.isEmpty()) {
      return false;
    }

    final String[] words = title.toLowerCase().split("\\s+");
    for (final String word : words) {
      if (badWords.contains(word.trim())) {
        return true;
      }
    }
    return false;
  }
}
