package mbm.classified_lifecycle.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import mbm.classified_lifecycle.classified.config.ExpirationDurationProperties;
import mbm.classified_lifecycle.config.IntegrationTestConfig;
import mbm.classified_lifecycle.dashboard.persistence.repository.ClassifiedHistoryReadOnlyRepository;
import mbm.classified_lifecycle.web.model.ClassifiedCategory;
import mbm.classified_lifecycle.web.model.ClassifiedRequest;
import mbm.classified_lifecycle.web.model.ClassifiedResponse;
import mbm.classified_lifecycle.web.model.ClassifiedStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class ClassifiedControllerIT extends IntegrationTestConfig {

  private static final String CREATE_CLASSIFIED_PATH = "/classifieds";
  private static final String ACTIVATE_CLASSIFIED_PATH = "/classifieds/%s/activate";
  private static final String DEACTIVATE_CLASSIFIED_PATH = "/classifieds/%s/deactivate";

  private static final String ESTATE_PENDING = "a1e4567e-e89b-12d3-a456-426614174001";
  private static final String VEHICLE_EXPIRED = "b2e4567e-e89b-12d3-a456-426614174002";
  private static final String SHOPPING_ACTIVE = "c3e4567e-e89b-12d3-a456-426614174003";

  private static final String TITLE = "dosta gider";
  private static final String DESCRIPTION = "mesajlari cevaplamiyorum, ciddi alicilar arasin";

  private static final String DUPLICATE_TITLE = "Estate Pending";
  private static final String DUPLICATE_DESCRIPTION = "Pending approval for estate classified";
  private static final String BAD_WORD = " opsiyonlu";

  @Autowired private ExpirationDurationProperties expirationDurationProperties;
  @Autowired private ClassifiedHistoryReadOnlyRepository classifiedHistoryReadOnlyRepository;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void test_createClassified_RealEstate() throws Exception {
    // GIVEN
    final ClassifiedRequest classifiedRequest =
        new ClassifiedRequest()
            .title(TITLE)
            .description(DESCRIPTION)
            .category(ClassifiedCategory.REAL_ESTATE);

    final String classifiedRequestJson = objectMapper.writeValueAsString(classifiedRequest);

    // WHEN
    final String responseJson =
        mockMvc
            .perform(
                post(CREATE_CLASSIFIED_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(classifiedRequestJson))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final ClassifiedResponse response =
        objectMapper.readValue(responseJson, ClassifiedResponse.class);
    assertThat(response.getId()).isNotNull();
    assertThat(response.getStatus()).isEqualTo(ClassifiedStatus.PENDING_APPROVAL);
    assertThat(response.getCategory()).isEqualTo(ClassifiedCategory.REAL_ESTATE);
    assertThat(response.getTitle()).isEqualTo(TITLE);
    assertThat(response.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(response.getCreatedAt()).isNotNull();
    assertThat(response.getExpiresAt())
        .isNotNull()
        .isEqualTo(response.getCreatedAt().plus(expirationDurationProperties.realEstate()));
  }

  @Test
  void test_createClassified_Vehicle() throws Exception {
    // GIVEN
    final ClassifiedRequest classifiedRequest =
        new ClassifiedRequest()
            .title(TITLE)
            .description(DESCRIPTION)
            .category(ClassifiedCategory.VEHICLE);

    final String classifiedRequestJson = objectMapper.writeValueAsString(classifiedRequest);

    // WHEN
    final String responseJson =
        mockMvc
            .perform(
                post(CREATE_CLASSIFIED_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(classifiedRequestJson))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final ClassifiedResponse response =
        objectMapper.readValue(responseJson, ClassifiedResponse.class);
    assertThat(response.getId()).isNotNull();
    assertThat(response.getStatus()).isEqualTo(ClassifiedStatus.PENDING_APPROVAL);
    assertThat(response.getCategory()).isEqualTo(ClassifiedCategory.VEHICLE);
    assertThat(response.getTitle()).isEqualTo(TITLE);
    assertThat(response.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(response.getCreatedAt()).isNotNull();
    assertThat(response.getExpiresAt())
        .isNotNull()
        .isEqualTo(response.getCreatedAt().plus(expirationDurationProperties.vehicle()));
  }

  @Test
  void test_createClassified_Shopping() throws Exception {
    // GIVEN
    final ClassifiedRequest classifiedRequest =
        new ClassifiedRequest()
            .title(TITLE)
            .description(DESCRIPTION)
            .category(ClassifiedCategory.SHOPPING);

    final String classifiedRequestJson = objectMapper.writeValueAsString(classifiedRequest);

    // WHEN
    final String responseJson =
        mockMvc
            .perform(
                post(CREATE_CLASSIFIED_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(classifiedRequestJson))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final ClassifiedResponse response =
        objectMapper.readValue(responseJson, ClassifiedResponse.class);
    assertThat(response.getId()).isNotNull();
    assertThat(response.getStatus()).isEqualTo(ClassifiedStatus.ACTIVE);
    assertThat(response.getCategory()).isEqualTo(ClassifiedCategory.SHOPPING);
    assertThat(response.getTitle()).isEqualTo(TITLE);
    assertThat(response.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(response.getCreatedAt()).isNotNull();
    assertThat(response.getExpiresAt())
        .isNotNull()
        .isEqualTo(response.getCreatedAt().plus(expirationDurationProperties.shopping()));
  }

  @Test
  void test_createClassified_Other() throws Exception {
    // GIVEN
    final ClassifiedRequest classifiedRequest =
        new ClassifiedRequest()
            .title(TITLE)
            .description(DESCRIPTION)
            .category(ClassifiedCategory.OTHER);

    final String classifiedRequestJson = objectMapper.writeValueAsString(classifiedRequest);

    // WHEN
    final String responseJson =
        mockMvc
            .perform(
                post(CREATE_CLASSIFIED_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(classifiedRequestJson))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final ClassifiedResponse response =
        objectMapper.readValue(responseJson, ClassifiedResponse.class);
    assertThat(response.getId()).isNotNull();
    assertThat(response.getStatus()).isEqualTo(ClassifiedStatus.PENDING_APPROVAL);
    assertThat(response.getCategory()).isEqualTo(ClassifiedCategory.OTHER);
    assertThat(response.getTitle()).isEqualTo(TITLE);
    assertThat(response.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(response.getCreatedAt()).isNotNull();
    assertThat(response.getExpiresAt())
        .isNotNull()
        .isEqualTo(response.getCreatedAt().plus(expirationDurationProperties.other()));
  }

  @Test
  void test_createClassified_Duplicate() throws Exception {
    // GIVEN
    final ClassifiedRequest classifiedRequest =
        new ClassifiedRequest()
            .title(DUPLICATE_TITLE)
            .description(DUPLICATE_DESCRIPTION)
            .category(ClassifiedCategory.REAL_ESTATE);

    final String classifiedRequestJson = objectMapper.writeValueAsString(classifiedRequest);

    // WHEN
    final String responseJson =
        mockMvc
            .perform(
                post(CREATE_CLASSIFIED_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(classifiedRequestJson))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final ClassifiedResponse response =
        objectMapper.readValue(responseJson, ClassifiedResponse.class);
    assertThat(response.getId()).isNotNull();
    assertThat(response.getStatus()).isEqualTo(ClassifiedStatus.DUPLICATE);
    assertThat(response.getCategory()).isEqualTo(ClassifiedCategory.REAL_ESTATE);
    assertThat(response.getTitle()).isEqualTo(DUPLICATE_TITLE);
    assertThat(response.getDescription()).isEqualTo(DUPLICATE_DESCRIPTION);
    assertThat(response.getCreatedAt()).isNotNull();
    assertThat(response.getExpiresAt())
        .isNotNull()
        .isEqualTo(response.getCreatedAt().plus(expirationDurationProperties.realEstate()));
  }

  @Test
  void test_createClassified_LongTitle() throws Exception {
    // GIVEN
    final ClassifiedRequest classifiedRequest =
        new ClassifiedRequest()
            .title(DUPLICATE_TITLE + DUPLICATE_TITLE + DUPLICATE_TITLE + DUPLICATE_TITLE)
            .description(DESCRIPTION)
            .category(ClassifiedCategory.REAL_ESTATE);

    final String classifiedRequestJson = objectMapper.writeValueAsString(classifiedRequest);

    // WHEN
    mockMvc
        .perform(
            post(CREATE_CLASSIFIED_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(classifiedRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.details", containsString("Field 'title' size must be between 10 and 50")));
  }

  @Test
  void test_createClassified_ShortDescription() throws Exception {
    // GIVEN
    final ClassifiedRequest classifiedRequest =
        new ClassifiedRequest()
            .title(TITLE)
            .description("Short")
            .category(ClassifiedCategory.REAL_ESTATE);

    final String classifiedRequestJson = objectMapper.writeValueAsString(classifiedRequest);

    // WHEN
    mockMvc
        .perform(
            post(CREATE_CLASSIFIED_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(classifiedRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath(
                "$.details",
                containsString("Field 'description' size must be between 20 and 200")));
  }

  @Test
  void test_createClassified_Badword() throws Exception {
    // GIVEN
    final ClassifiedRequest classifiedRequest =
        new ClassifiedRequest()
            .title(TITLE + BAD_WORD)
            .description(DESCRIPTION)
            .category(ClassifiedCategory.REAL_ESTATE);

    final String classifiedRequestJson = objectMapper.writeValueAsString(classifiedRequest);

    // WHEN
    mockMvc
        .perform(
            post(CREATE_CLASSIFIED_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(classifiedRequestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details", containsString("Field 'title' contains forbidden words")))
        .andExpect(jsonPath("$.details", containsString(BAD_WORD)));
  }

  @Test
  void test_createClassified_Badword_OnDescription() throws Exception {
    // GIVEN
    final ClassifiedRequest classifiedRequest =
        new ClassifiedRequest()
            .title(TITLE)
            .description(DESCRIPTION + BAD_WORD)
            .category(ClassifiedCategory.REAL_ESTATE);

    final String classifiedRequestJson = objectMapper.writeValueAsString(classifiedRequest);

    // WHEN
    mockMvc
        .perform(
            post(CREATE_CLASSIFIED_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(classifiedRequestJson))
        .andExpect(status().isCreated());
  }

  @Test
  void test_activateClassified_Pending() throws Exception {
    // GIVEN
    final String activatePath = String.format(ACTIVATE_CLASSIFIED_PATH, ESTATE_PENDING);

    // WHEN
    final String responseJson =
        mockMvc
            .perform(post(activatePath).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final ClassifiedResponse response =
        objectMapper.readValue(responseJson, ClassifiedResponse.class);

    assertThat(response.getId()).hasToString(ESTATE_PENDING);
    assertThat(response.getStatus()).isEqualTo(ClassifiedStatus.ACTIVE);
    assertThat(response.getCategory()).isEqualTo(ClassifiedCategory.REAL_ESTATE);

    final var historyList =
        classifiedHistoryReadOnlyRepository.findByClassifiedIdOrderByChangedAtDesc(
            UUID.fromString(ESTATE_PENDING));
    assertThat(historyList)
        .anySatisfy(
            history -> {
              assertThat(history.getPreviousStatus())
                  .hasToString(ClassifiedStatus.PENDING_APPROVAL.getValue());
              assertThat(history.getNewStatus()).hasToString(ClassifiedStatus.ACTIVE.getValue());
            });
  }

  @Test
  void test_activateClassified_Active_shouldReturnBadRequest() throws Exception {
    // GIVEN
    final String activatePath = String.format(ACTIVATE_CLASSIFIED_PATH, SHOPPING_ACTIVE);

    // WHEN & THEN
    mockMvc
        .perform(post(activatePath).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("Cannot activate classified ad")));
  }

  @Test
  void test_activateClassified_NotFound() throws Exception {
    // GIVEN
    final String randomId = UUID.randomUUID().toString();
    final String activatePath = String.format(ACTIVATE_CLASSIFIED_PATH, randomId);

    // WHEN & THEN
    mockMvc
        .perform(post(activatePath).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void test_deactivateClassified_Active() throws Exception {
    // GIVEN
    final String deactivatePath = String.format(DEACTIVATE_CLASSIFIED_PATH, SHOPPING_ACTIVE);

    // WHEN
    final String responseJson =
        mockMvc
            .perform(post(deactivatePath).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final ClassifiedResponse response =
        objectMapper.readValue(responseJson, ClassifiedResponse.class);

    assertThat(response.getId()).hasToString(SHOPPING_ACTIVE);
    assertThat(response.getStatus()).isEqualTo(ClassifiedStatus.INACTIVE);
    assertThat(response.getCategory()).isEqualTo(ClassifiedCategory.SHOPPING);

    final var historyList =
        classifiedHistoryReadOnlyRepository.findByClassifiedIdOrderByChangedAtDesc(
            UUID.fromString(SHOPPING_ACTIVE));
    assertThat(historyList)
        .anySatisfy(
            history -> {
              assertThat(history.getPreviousStatus())
                  .hasToString(ClassifiedStatus.ACTIVE.getValue());
              assertThat(history.getNewStatus()).hasToString(ClassifiedStatus.INACTIVE.getValue());
            });
  }

  @Test
  void test_deactivateClassified_Pending() throws Exception {
    // GIVEN
    final String deactivatePath = String.format(DEACTIVATE_CLASSIFIED_PATH, ESTATE_PENDING);

    // WHEN
    final String responseJson =
        mockMvc
            .perform(post(deactivatePath).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final ClassifiedResponse response =
        objectMapper.readValue(responseJson, ClassifiedResponse.class);

    assertThat(response.getId()).hasToString(ESTATE_PENDING);
    assertThat(response.getStatus()).isEqualTo(ClassifiedStatus.INACTIVE);
    assertThat(response.getCategory()).isEqualTo(ClassifiedCategory.REAL_ESTATE);

    final var historyList =
        classifiedHistoryReadOnlyRepository.findByClassifiedIdOrderByChangedAtDesc(
            UUID.fromString(ESTATE_PENDING));
    assertThat(historyList)
        .anySatisfy(
            history -> {
              assertThat(history.getPreviousStatus())
                  .hasToString(ClassifiedStatus.PENDING_APPROVAL.getValue());
              assertThat(history.getNewStatus()).hasToString(ClassifiedStatus.INACTIVE.getValue());
            });
  }

  @Test
  void test_deactivateClassified_Expired_shouldReturnBadRequest() throws Exception {
    // GIVEN
    final String deactivatePath = String.format(DEACTIVATE_CLASSIFIED_PATH, VEHICLE_EXPIRED);

    // WHEN & THEN
    mockMvc
        .perform(post(deactivatePath).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("Cannot deactivate classified ad")));
  }

  @Test
  void test_deactivateClassified_NotFound() throws Exception {
    // GIVEN
    final String randomId = UUID.randomUUID().toString();
    final String deactivatePath = String.format(DEACTIVATE_CLASSIFIED_PATH, randomId);

    // WHEN & THEN
    mockMvc
        .perform(post(deactivatePath).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
