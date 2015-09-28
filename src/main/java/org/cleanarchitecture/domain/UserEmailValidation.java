package org.cleanarchitecture.domain;

import org.cleanarchitecture.common.dao.RepositoryRegistry;
import org.cleanarchitecture.dao.UserRepository;

import javax.validation.ConstraintViolationException;


public class UserEmailValidation {
    public static void validateNoCollisionWithLoginId(String email) {
        UserRepository userRepository = RepositoryRegistry.repository(UserRepository.class);
        if (userRepository.existsByLoginId(email)) {
            throw new ConstraintViolationException("Email collides with an existing LOGIN ID: " + email, null);
        }
    }

    public static void validateNoCollisionWithContactEmails(String email) {
        UserRepository userRepository = RepositoryRegistry.repository(UserRepository.class);
        if (userRepository.existsByContactEmail(email)) {
            throw new ConstraintViolationException("Email collides with an existing LOGIN ID: " + email, null);
        }
    }
}
