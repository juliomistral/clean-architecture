package com.hci.common.domain.validation;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class CollectionValuesAreEmailValidator implements ConstraintValidator<ValuesAreEmail, Set<String>> {
    private EmailValidator emailValidator;

    @Override
    public void initialize(ValuesAreEmail constraintAnnotation) {
        this.emailValidator = new EmailValidator();
    }

    @Override
    public boolean isValid(Set  <String> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        for (String item : value) {
            if (!emailValidator.isValid(item, context)) {
                return false;
            }
        }
        return true;
    }
}
