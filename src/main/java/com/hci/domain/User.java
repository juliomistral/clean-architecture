package com.hci.domain;


import com.hci.common.RepositoryRegistry;
import com.hci.dao.UserRepository;
import com.hci.domain.base.Entity;
import org.hibernate.validator.constraints.Email;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

public class User extends Entity<User> {
    private String firstName;
    private String lastName;
    private String ssn;
    @NotNull @Email private String loginId;


    public User firstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User lastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User loginId(final String loginId) {
        this.loginId = loginId;
        return this;
    }

    public User ssn(final String ssn) {
        this.ssn = ssn;
        return this;
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

    public void updateLoginId(String newLoginId) {
        UserRepository userRepository = RepositoryRegistry.repository(UserRepository.class);
        if (userRepository.existsByLoginId(newLoginId)) {
            throw new ValidationException("User already exists with login: " + newLoginId);
        }

        this.loginId = newLoginId;
    }
}
