package dev.profitsoft.internship.rebrov.blocktwo.validation;

import dev.profitsoft.internship.rebrov.blocktwo.dto.MovieQueryDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IntervalValidator implements ConstraintValidator<ValidIntervals, MovieQueryDto> {

    @Override
    public boolean isValid(MovieQueryDto dto, ConstraintValidatorContext context) {

        if (dto.getMinRating() != null && dto.getMaxRating() != null) {
            if (dto.getMinRating() > dto.getMaxRating()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("minRating cannot be greater than maxRating")
                        .addPropertyNode("minRating")
                        .addConstraintViolation();
                return false;
            }
        }

        if (dto.getMinYear() != null && dto.getMaxYear() != null) {
            if (dto.getMinYear() > dto.getMaxYear()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("minYear cannot be greater than maxYear")
                        .addPropertyNode("minYear")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}

