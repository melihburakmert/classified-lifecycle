package mbm.classified_lifecycle.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.create;
import static org.mockito.Mockito.when;

import java.util.UUID;
import mbm.classified_lifecycle.classified.ClassifiedDto;
import mbm.classified_lifecycle.classified.ClassifiedRequestDto;
import mbm.classified_lifecycle.classified.ClassifiedService;
import mbm.classified_lifecycle.web.mapper.ClassifiedRequestMapper;
import mbm.classified_lifecycle.web.mapper.ClassifiedResponseMapper;
import mbm.classified_lifecycle.web.model.ClassifiedRequest;
import mbm.classified_lifecycle.web.model.ClassifiedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
class ClassifiedsControllerUT {

  @Mock private ClassifiedService classifiedService;
  @Mock private ClassifiedRequestMapper classifiedRequestMapper;
  @Mock private ClassifiedResponseMapper classifiedResponseMapper;

  private ClassifiedsController classifiedsController;

  @BeforeEach
  void setUp() {
    classifiedsController =
        new ClassifiedsController(
            classifiedService, classifiedRequestMapper, classifiedResponseMapper);
  }

  @Test
  void test_createClassified() {
    // GIVEN
    final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

    final ClassifiedRequest request = create(ClassifiedRequest.class);
    final ClassifiedRequestDto requestDto = create(ClassifiedRequestDto.class);
    final ClassifiedDto classifiedDto = create(ClassifiedDto.class);
    final ClassifiedResponse response = create(ClassifiedResponse.class);

    when(classifiedRequestMapper.map(request)).thenReturn(requestDto);
    when(classifiedService.createClassified(requestDto)).thenReturn(classifiedDto);
    when(classifiedResponseMapper.map(classifiedDto)).thenReturn(response);

    // WHEN
    final ResponseEntity<ClassifiedResponse> entity =
        classifiedsController.createClassified(request);

    // THEN
    assertThat(entity).isNotNull();
    assertThat(entity.getStatusCodeValue()).isEqualTo(201);
    assertThat(entity.getBody()).isEqualTo(response);
    assertThat(entity.getHeaders().getLocation()).isNotNull();
    assertThat(entity.getHeaders().getLocation().toString())
        .contains(classifiedDto.getId().toString());

    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void test_activateClassified() {
    // GIVEN
    final UUID id = UUID.randomUUID();
    final ClassifiedDto activatedDto = create(ClassifiedDto.class);
    final ClassifiedResponse response = create(ClassifiedResponse.class);

    when(classifiedService.activateClassified(id)).thenReturn(activatedDto);
    when(classifiedResponseMapper.map(activatedDto)).thenReturn(response);

    // WHEN
    final ResponseEntity<ClassifiedResponse> entity = classifiedsController.activateClassified(id);

    // THEN
    assertThat(entity).isNotNull();
    assertThat(entity.getStatusCodeValue()).isEqualTo(200);
    assertThat(entity.getBody()).isEqualTo(response);
  }

  @Test
  void test_deactivateClassified() {
    // GIVEN
    final UUID id = UUID.randomUUID();
    final ClassifiedDto deactivatedDto = create(ClassifiedDto.class);
    final ClassifiedResponse response = create(ClassifiedResponse.class);

    when(classifiedService.deactivateClassified(id)).thenReturn(deactivatedDto);
    when(classifiedResponseMapper.map(deactivatedDto)).thenReturn(response);

    // WHEN
    final ResponseEntity<ClassifiedResponse> entity =
        classifiedsController.deactivateClassified(id);

    // THEN
    assertThat(entity).isNotNull();
    assertThat(entity.getStatusCodeValue()).isEqualTo(200);
    assertThat(entity.getBody()).isEqualTo(response);
  }
}
