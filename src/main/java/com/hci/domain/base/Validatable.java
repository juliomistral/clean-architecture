package com.hci.domain.base;

import javax.validation.ConstraintViolationException;

public interface Validatable {
    void validate() throws ConstraintViolationException;
}
