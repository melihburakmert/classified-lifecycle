package mbm.classified_lifecycle.web;

import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mbm.classified_lifecycle.classified.ClassifiedDto;
import mbm.classified_lifecycle.classified.ClassifiedRequestDto;
import mbm.classified_lifecycle.classified.ClassifiedService;
import mbm.classified_lifecycle.web.api.ClassifiedsApiDelegate;
import mbm.classified_lifecycle.web.mapper.ClassifiedRequestMapper;
import mbm.classified_lifecycle.web.mapper.ClassifiedResponseMapper;
import mbm.classified_lifecycle.web.model.ClassifiedRequest;
import mbm.classified_lifecycle.web.model.ClassifiedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class ClassifiedsController implements ClassifiedsApiDelegate {

  private final ClassifiedService classifiedService;
  private final ClassifiedRequestMapper classifiedRequestMapper;
  private final ClassifiedResponseMapper classifiedResponseMapper;

  @Override
  public ResponseEntity<ClassifiedResponse> createClassified(
      final ClassifiedRequest classifiedRequest) {

    final ClassifiedRequestDto classifiedRequestDto =
        classifiedRequestMapper.map(classifiedRequest);
    final ClassifiedDto classifiedDto = classifiedService.createClassified(classifiedRequestDto);
    final ClassifiedResponse classifiedResponse = classifiedResponseMapper.map(classifiedDto);
    final URI location = getCreatedLocation(classifiedDto);

    return ResponseEntity.created(location).body(classifiedResponse);
  }

  @Override
  public ResponseEntity<ClassifiedResponse> activateClassified(final UUID id) {

    final ClassifiedDto activatedDto = classifiedService.activateClassified(id);
    final ClassifiedResponse response = classifiedResponseMapper.map(activatedDto);

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ClassifiedResponse> deactivateClassified(final UUID id) {

    final ClassifiedDto deactivatedDto = classifiedService.deactivateClassified(id);
    final ClassifiedResponse response = classifiedResponseMapper.map(deactivatedDto);

    return ResponseEntity.ok(response);
  }

  private URI getCreatedLocation(final ClassifiedDto classifiedDto) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(classifiedDto.id())
        .toUri();
  }
}
