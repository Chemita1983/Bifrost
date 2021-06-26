package com.bifrost.aplication.validations;

import com.bifrost.aplication.domain.BifrostEntity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * Generic class to validate Bifrost entities
 *
 * @param <A> Annotation provided
 * @param <T> Object to validate
 */
public abstract class BifrostValidator<A extends Annotation, T extends BifrostEntity> implements ConstraintValidator<A, T> {

    @Override
    public boolean isValid(T t, ConstraintValidatorContext constraintValidatorContext) {
        return validBifrostEntity(t);
    }

    public abstract boolean validBifrostEntity(T t);
}
