package com.hci.domain;


import com.hci.common.RepositoryRegistry;
import com.hci.dao.UserRepository;
import com.hci.domain.base.Entity;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

public class User extends Entity<User> {
    private String firstName;
    private String lastName;
    private String ssn;
    private String loginId;

    public User(String firstName, String lastName, String ssn, String loginId) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.loginId = loginId;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getLoginId() {
        return loginId;
    }
    public String getSsn() {
        return ssn;
    }


    @Override
    public void validate() throws ConstraintViolationException {
        validateLoginId(this.loginId);
    }

    public void updateLoginId(String newLoginId) {
        validateLoginId(newLoginId);
        this.loginId = newLoginId;
    }

    private void validateLoginId(String newLoginId) {
        UserRepository userRepository = RepositoryRegistry.repository(UserRepository.class);
        if (userRepository.existsByLoginId(newLoginId)) {
            throw new ValidationException("User already exists with login: " + newLoginId);
        }
    }
}
