package mbm.classified_lifecycle.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import mbm.classified_lifecycle.config.IntegrationTestConfig;
import mbm.classified_lifecycle.web.model.ClassifiedStatisticsResponse;
import mbm.classified_lifecycle.web.model.ClassifiedStatus;
import mbm.classified_lifecycle.web.model.ClassifiedStatusHistoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class DashboardControllerIT extends IntegrationTestConfig {

  private static final String DASHBOARD_CLASSIFIED_STATUS_HISTORY_PATH =
      "/dashboard/classifieds/%s/history";
  private static final String DASHBOARD_CLASSIFIED_STATISTICS_PATH =
      "/dashboard/classifieds/statistics";

  private static final String VEHICLE_EXPIRED = "b2e4567e-e89b-12d3-a456-426614174002";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void test_getClassifiedStatusHistory() throws Exception {
    // GIVEN

    // WHEN
    final String path = String.format(DASHBOARD_CLASSIFIED_STATUS_HISTORY_PATH, VEHICLE_EXPIRED);
    final String responseJson =
        mockMvc
            .perform(get(path).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final List<ClassifiedStatusHistoryResponse> responses =
        objectMapper.readerForListOf(ClassifiedStatusHistoryResponse.class).readValue(responseJson);

    assertThat(responses).hasSize(4);

    assertThat(responses.get(0).getPreviousStatus())
        .hasToString(ClassifiedStatus.INACTIVE.getValue());
    assertThat(responses.get(0).getNewStatus()).hasToString(ClassifiedStatus.EXPIRED.getValue());

    assertThat(responses.get(1).getPreviousStatus())
        .hasToString(ClassifiedStatus.ACTIVE.getValue());
    assertThat(responses.get(1).getNewStatus()).hasToString(ClassifiedStatus.INACTIVE.getValue());

    assertThat(responses.get(2).getPreviousStatus())
        .hasToString(ClassifiedStatus.PENDING_APPROVAL.getValue());
    assertThat(responses.get(2).getNewStatus()).hasToString(ClassifiedStatus.ACTIVE.getValue());

    assertThat(responses.get(3).getPreviousStatus()).isNull();
    assertThat(responses.get(3).getNewStatus())
        .hasToString(ClassifiedStatus.PENDING_APPROVAL.getValue());
  }

  @Test
  void test_getClassifiedStatusHistory_NotFound() throws Exception {
    // GIVEN
    final String randomId = UUID.randomUUID().toString();

    // WHEN
    final String path = String.format(DASHBOARD_CLASSIFIED_STATUS_HISTORY_PATH, randomId);
    mockMvc
        .perform(get(path).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.details", containsString(randomId)));

    // THEN
  }

  @Test
  void test_getClassifiedStatistics() throws Exception {
    // GIVEN

    // WHEN
    final String responseJson =
        mockMvc
            .perform(
                get(DASHBOARD_CLASSIFIED_STATISTICS_PATH).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // THEN
    final ClassifiedStatisticsResponse response =
        objectMapper.readValue(responseJson, ClassifiedStatisticsResponse.class);

    assertThat(response.getCountByStatus())
        .containsEntry(ClassifiedStatus.PENDING_APPROVAL.getValue(), 1)
        .containsEntry(ClassifiedStatus.ACTIVE.getValue(), 2)
        .containsEntry(ClassifiedStatus.EXPIRED.getValue(), 1)
        .doesNotContainKey(ClassifiedStatus.INACTIVE.getValue())
        .doesNotContainKey(ClassifiedStatus.DUPLICATE.getValue());
  }
}
