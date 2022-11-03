package com.prominent.title.annotation;

import com.nimbusds.jose.Payload;
import com.prominent.title.validation.ResetPasswordValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ResetPasswordValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResetPasswordConstraint {

    String message() default "Password Should Contain At Least 1 Capital letter, 1 Symbol and 1 Digit";

    Class<?>[] groups() default {};

    Class<Payload>[] payload() default {};
}
