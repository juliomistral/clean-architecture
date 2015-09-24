package com.hci.test.matchers;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

public class ConstraintViolationsMatcher extends TypeSafeMatcher<ConstraintViolationException> {

    public static ConstraintViolationsMatcher hasViolation(String propertyName, String message) {
        return new ConstraintViolationsMatcher(propertyName, message, true);
    }

    public static ConstraintViolationsMatcher isViolationException(String message) {
        return new ConstraintViolationsMatcher(null, message, false);
    }

    private String propertyName;
    private String message;
    private String actualPropertyName;
    private String actualMessage;
    private ConstraintViolation violation;
    private boolean isCheckingForViolations;

    public ConstraintViolationsMatcher(String propertyName, String message, boolean isCheckingForViolations) {
        this.propertyName = propertyName;
        this.message = message;
        this.isCheckingForViolations = isCheckingForViolations;
    }

    @Override
    protected boolean matchesSafely(ConstraintViolationException exec) {
        if (isCheckingForViolations) {
            return checkForExpectedViolation(exec);
        } else {
            this.actualMessage = exec.getMessage();
            return exec.getMessage().equals(message);
        }
    }

    private boolean checkForExpectedViolation(ConstraintViolationException exec) {
        this.violation = findViolation(exec.getConstraintViolations());
        if (this.violation == null) {
            return false;
        }

        this.actualPropertyName = extractPropertyName(violation);
        this.actualMessage = violation.getMessage();

        return actualPropertyName.equals(propertyName) && actualMessage.equals(message);
    }

    private String extractPropertyName(ConstraintViolation violation) {
        return violation.getPropertyPath().iterator().next().getName();
    }

    private ConstraintViolation findViolation(Set<ConstraintViolation<?>> constraintViolations) {
        for (ConstraintViolation violation : constraintViolations) {
            String propertyName = extractPropertyName(violation);
            if (propertyName.equals(this.propertyName)) {
                return violation;
            }
        }
        return null;
    }

    @Override
    public void describeTo(Description description) {
        if (this.isCheckingForViolations && this.violation == null) {
            description.appendText("Could not find a violation for property: ").appendValue(propertyName);
            return;
        }

        description
            .appendText("Violation message mismatch: Expected = ")
            .appendValue(message)
            .appendText(", Actual = ")
            .appendValue(actualMessage);
    }
}
