package github.com.josuemarcos.libraryapi.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AutorValidator.class)
public @interface ValidAuthor {
    String message() default "Autor inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
