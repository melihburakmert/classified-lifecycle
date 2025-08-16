package mbm.classified_lifecycle.classified.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Instancio.create;
import static org.instancio.Instancio.of;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import mbm.classified_lifecycle.classified.ClassifiedDto;
import mbm.classified_lifecycle.classified.ClassifiedRequestDto;
import mbm.classified_lifecycle.classified.exception.InvalidActivateException;
import mbm.classified_lifecycle.classified.exception.InvalidDeactivateException;
import mbm.classified_lifecycle.classified.persistence.entity.ClassifiedEntity;
import mbm.classified_lifecycle.classified.persistence.mapper.ClassifiedEntityMapper;
import mbm.classified_lifecycle.classified.persistence.repository.ClassifiedRepository;
import mbm.classified_lifecycle.common.ClassifiedCategory;
import mbm.classified_lifecycle.common.ClassifiedNotFoundException;
import mbm.classified_lifecycle.common.ClassifiedStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassifiedServiceUT {

  @Mock private ClassifiedRepository classifiedRepository;
  @Mock private ClassifiedEntityMapper classifiedEntityToDtoMapper;
  @Mock private ClassifiedExpirationCalculator classifiedExpirationCalculator;

  private ClassifiedServiceImp classifiedService;

  @BeforeEach
  void setUp() {
    classifiedService =
        new ClassifiedServiceImp(
            classifiedRepository, classifiedEntityToDtoMapper, classifiedExpirationCalculator);
  }

  @Test
  void test_createClassified_categoryShopping() {
    // GIVEN
    final ClassifiedRequestDto requestDto =
        of(ClassifiedRequestDto.class)
            .set(field(ClassifiedRequestDto::getCategory), ClassifiedCategory.SHOPPING)
            .create();
    final ClassifiedEntity savedEntity = create(ClassifiedEntity.class);
    final ClassifiedDto dto = create(ClassifiedDto.class);

    when(classifiedRepository.existsByTitleAndDescriptionAndCategory(
            requestDto.getTitle(), requestDto.getDescription(), requestDto.getCategory()))
        .thenReturn(false);
    when(classifiedExpirationCalculator.calculateExpirationDate(
            eq(requestDto.getCategory()), any()))
        .thenReturn(create(Instant.class));
    when(classifiedRepository.save(any(ClassifiedEntity.class))).thenReturn(savedEntity);
    when(classifiedEntityToDtoMapper.map(savedEntity)).thenReturn(dto);

    // WHEN
    final ClassifiedDto result = classifiedService.createClassified(requestDto);

    // THEN
    assertThat(result).isEqualTo(dto);

    verify(classifiedRepository)
        .existsByTitleAndDescriptionAndCategory(
            requestDto.getTitle(), requestDto.getDescription(), requestDto.getCategory());
    verify(classifiedExpirationCalculator)
        .calculateExpirationDate(eq(requestDto.getCategory()), any());
    verify(classifiedEntityToDtoMapper).map(savedEntity);

    final ArgumentCaptor<ClassifiedEntity> captor = ArgumentCaptor.forClass(ClassifiedEntity.class);
    verify(classifiedRepository).save(captor.capture());
    final ClassifiedEntity capturedEntity = captor.getValue();
    assertThat(capturedEntity.getTitle()).isEqualTo(requestDto.getTitle());
    assertThat(capturedEntity.getDescription()).isEqualTo(requestDto.getDescription());
    assertThat(capturedEntity.getCategory()).isEqualTo(requestDto.getCategory());
  }

  @ParameterizedTest
  @EnumSource(
      value = ClassifiedCategory.class,
      names = {"REAL_ESTATE", "VEHICLE", "OTHER"})
  void test_createClassified_categoryNonShopping(final ClassifiedCategory category) {
    // GIVEN
    final ClassifiedRequestDto requestDto =
        of(ClassifiedRequestDto.class)
            .set(field(ClassifiedRequestDto::getCategory), category)
            .create();
    final ClassifiedEntity savedEntity = create(ClassifiedEntity.class);
    final ClassifiedDto dto = create(ClassifiedDto.class);

    when(classifiedRepository.existsByTitleAndDescriptionAndCategory(
            requestDto.getTitle(), requestDto.getDescription(), requestDto.getCategory()))
        .thenReturn(false);
    when(classifiedExpirationCalculator.calculateExpirationDate(
            eq(requestDto.getCategory()), any()))
        .thenReturn(create(Instant.class));
    when(classifiedRepository.save(any(ClassifiedEntity.class))).thenReturn(savedEntity);
    when(classifiedEntityToDtoMapper.map(savedEntity)).thenReturn(dto);

    // WHEN
    final ClassifiedDto result = classifiedService.createClassified(requestDto);

    // THEN
    assertThat(result).isEqualTo(dto);

    verify(classifiedRepository)
        .existsByTitleAndDescriptionAndCategory(
            requestDto.getTitle(), requestDto.getDescription(), requestDto.getCategory());
    verify(classifiedExpirationCalculator)
        .calculateExpirationDate(eq(requestDto.getCategory()), any());
    verify(classifiedEntityToDtoMapper).map(savedEntity);

    final ArgumentCaptor<ClassifiedEntity> captor = ArgumentCaptor.forClass(ClassifiedEntity.class);
    verify(classifiedRepository).save(captor.capture());
    final ClassifiedEntity capturedEntity = captor.getValue();
    assertThat(capturedEntity.getTitle()).isEqualTo(requestDto.getTitle());
    assertThat(capturedEntity.getDescription()).isEqualTo(requestDto.getDescription());
    assertThat(capturedEntity.getCategory()).isEqualTo(requestDto.getCategory());
    assertThat(capturedEntity.getStatus()).isEqualTo(ClassifiedStatus.PENDING_APPROVAL);
  }

  @Test
  void test_createClassified_isDuplicate() {
    // GIVEN
    final ClassifiedRequestDto requestDto = create(ClassifiedRequestDto.class);
    final ClassifiedEntity savedEntity = create(ClassifiedEntity.class);
    final ClassifiedDto dto = create(ClassifiedDto.class);

    when(classifiedRepository.existsByTitleAndDescriptionAndCategory(
            requestDto.getTitle(), requestDto.getDescription(), requestDto.getCategory()))
        .thenReturn(true);
    when(classifiedExpirationCalculator.calculateExpirationDate(
            eq(requestDto.getCategory()), any()))
        .thenReturn(create(Instant.class));
    when(classifiedRepository.save(any(ClassifiedEntity.class))).thenReturn(savedEntity);
    when(classifiedEntityToDtoMapper.map(savedEntity)).thenReturn(dto);

    // WHEN
    final ClassifiedDto result = classifiedService.createClassified(requestDto);

    // THEN
    assertThat(result).isEqualTo(dto);

    verify(classifiedRepository)
        .existsByTitleAndDescriptionAndCategory(
            requestDto.getTitle(), requestDto.getDescription(), requestDto.getCategory());
    verify(classifiedExpirationCalculator)
        .calculateExpirationDate(eq(requestDto.getCategory()), any());
    verify(classifiedEntityToDtoMapper).map(savedEntity);

    final ArgumentCaptor<ClassifiedEntity> captor = ArgumentCaptor.forClass(ClassifiedEntity.class);
    verify(classifiedRepository).save(captor.capture());
    final ClassifiedEntity capturedEntity = captor.getValue();
    assertThat(capturedEntity.getTitle()).isEqualTo(requestDto.getTitle());
    assertThat(capturedEntity.getDescription()).isEqualTo(requestDto.getDescription());
    assertThat(capturedEntity.getCategory()).isEqualTo(requestDto.getCategory());
    assertThat(capturedEntity.getStatus()).isEqualTo(ClassifiedStatus.DUPLICATE);
  }

  @Test
  void test_activateClassified() {
    // GIVEN
    final UUID id = UUID.randomUUID();
    final ClassifiedEntity entity =
        of(ClassifiedEntity.class)
            .set(field(ClassifiedEntity::getId), id)
            .set(field(ClassifiedEntity::getStatus), ClassifiedStatus.PENDING_APPROVAL)
            .create();
    final ClassifiedEntity savedEntity = create(ClassifiedEntity.class);
    final ClassifiedDto dto = create(ClassifiedDto.class);

    when(classifiedRepository.findById(id)).thenReturn(Optional.of(entity));
    when(classifiedRepository.save(entity)).thenReturn(savedEntity);
    when(classifiedEntityToDtoMapper.map(savedEntity)).thenReturn(dto);

    // WHEN
    final ClassifiedDto result = classifiedService.activateClassified(id);

    // THEN
    assertThat(result).isEqualTo(dto);
    assertThat(entity.getStatus()).isEqualTo(ClassifiedStatus.ACTIVE);

    verify(classifiedRepository).findById(id);
    verify(classifiedRepository).save(entity);
    verify(classifiedEntityToDtoMapper).map(savedEntity);
  }

  @Test
  void test_activateClassified_statusPending() {
    // GIVEN
    final UUID id = UUID.randomUUID();
    final ClassifiedEntity entity =
        of(ClassifiedEntity.class)
            .set(field(ClassifiedEntity::getId), id)
            .generate(
                field(ClassifiedEntity::getStatus),
                gen ->
                    gen.oneOf(
                        ClassifiedStatus.ACTIVE,
                        ClassifiedStatus.INACTIVE,
                        ClassifiedStatus.DUPLICATE,
                        ClassifiedStatus.EXPIRED))
            .create();

    when(classifiedRepository.findById(id)).thenReturn(Optional.of(entity));

    // WHEN / THEN
    assertThatThrownBy(() -> classifiedService.activateClassified(id))
        .isInstanceOf(InvalidActivateException.class);

    verify(classifiedRepository).findById(id);
    verify(classifiedRepository, never()).save(any(ClassifiedEntity.class));
    verify(classifiedEntityToDtoMapper, never()).map(any(ClassifiedEntity.class));
  }

  @Test
  void test_activateClassified_notFound() {
    final UUID id = UUID.randomUUID();
    when(classifiedRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> classifiedService.activateClassified(id))
        .isInstanceOf(ClassifiedNotFoundException.class);

    verify(classifiedRepository).findById(id);
    verify(classifiedRepository, never()).save(any(ClassifiedEntity.class));
    verify(classifiedEntityToDtoMapper, never()).map(any(ClassifiedEntity.class));
  }

  @ParameterizedTest
  @EnumSource(
      value = ClassifiedStatus.class,
      names = {"ACTIVE", "PENDING_APPROVAL"})
  void test_deactivateClassified_statusActiveOrPending(final ClassifiedStatus status) {
    // GIVEN
    final UUID id = UUID.randomUUID();
    final ClassifiedEntity entity =
        of(ClassifiedEntity.class)
            .set(field(ClassifiedEntity::getId), id)
            .set(field(ClassifiedEntity::getStatus), status)
            .create();
    final ClassifiedEntity savedEntity = create(ClassifiedEntity.class);
    final ClassifiedDto dto = create(ClassifiedDto.class);

    when(classifiedRepository.findById(id)).thenReturn(Optional.of(entity));
    when(classifiedRepository.save(entity)).thenReturn(savedEntity);
    when(classifiedEntityToDtoMapper.map(savedEntity)).thenReturn(dto);

    // WHEN
    final ClassifiedDto result = classifiedService.deactivateClassified(id);

    // THEN
    assertThat(result).isEqualTo(dto);
    assertThat(entity.getStatus()).isEqualTo(ClassifiedStatus.INACTIVE);

    verify(classifiedRepository).findById(id);
    verify(classifiedRepository).save(entity);
    verify(classifiedEntityToDtoMapper).map(savedEntity);
  }

  @ParameterizedTest
  @EnumSource(
      value = ClassifiedStatus.class,
      names = {"INACTIVE", "DUPLICATE", "EXPIRED"})
  void test_deactivateClassified_statusNotActiveOrPending(final ClassifiedStatus status) {
    // GIVEN
    final UUID id = UUID.randomUUID();
    final ClassifiedEntity entity =
        of(ClassifiedEntity.class)
            .set(field(ClassifiedEntity::getId), id)
            .set(field(ClassifiedEntity::getStatus), status)
            .create();

    when(classifiedRepository.findById(id)).thenReturn(Optional.of(entity));

    // WHEN / THEN
    assertThatThrownBy(() -> classifiedService.deactivateClassified(id))
        .isInstanceOf(InvalidDeactivateException.class);

    verify(classifiedRepository).findById(id);
    verify(classifiedRepository, never()).save(any(ClassifiedEntity.class));
    verify(classifiedEntityToDtoMapper, never()).map(any(ClassifiedEntity.class));
  }

  @Test
  void test_deactivateClassified_notFound() {
    final UUID id = UUID.randomUUID();
    when(classifiedRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> classifiedService.deactivateClassified(id))
        .isInstanceOf(ClassifiedNotFoundException.class);

    verify(classifiedRepository).findById(id);
    verify(classifiedRepository, never()).save(any(ClassifiedEntity.class));
    verify(classifiedEntityToDtoMapper, never()).map(any(ClassifiedEntity.class));
  }
}
