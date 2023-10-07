package br.app.cashew.global.hibernate.constraints.uf;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UFCaseInsensitiveValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUF {
    String message() default "UF inválida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
