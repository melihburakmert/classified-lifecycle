package mbm.classified_lifecycle.web.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ClassifiedRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidClassifiedRequest {
  String message() default "Invalid classified request";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
