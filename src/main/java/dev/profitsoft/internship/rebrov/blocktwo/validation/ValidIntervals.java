package dev.profitsoft.internship.rebrov.blocktwo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntervalValidator.class)
public @interface ValidIntervals {
    String message() default "Invalid interval values";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
