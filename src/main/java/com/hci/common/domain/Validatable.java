package com.hci.common.domain;

import javax.validation.ConstraintViolationException;

public interface Validatable {

    void validate() throws ConstraintViolationException;
}
