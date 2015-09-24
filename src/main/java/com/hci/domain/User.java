package com.hci.domain;

import com.hci.common.dao.RepositoryRegistry;
import com.hci.common.domain.Entity;
import com.hci.common.domain.Id;
import com.hci.dao.UserRepository;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class User extends Entity<User> {
    @Size(max = 256, message = "First name cannot be longer than {max} characters long")
    private String firstName;

    @Size(max = 256, message = "Last name cannot be longer than {max} characters long")
    private String lastName;

    @NotNull(message = "Login ID cannot be null")
    @Email(message = "Login ID must be an email.  Invalid value = ${validatedValue}")
    private String loginId;

    private Set<String> contactEmails;


    public User(String firstName, String lastName, String loginId, Set<String> contactEmails) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginId = loginId;
        this.contactEmails = contactEmails;
    }

    public User(Id<User> id,
                int version,
                Date created,
                Date updated,
                String firstName,
                String lastName,
                String loginId,
                Set<String> contactEmails) {
        super(id, version, created, updated);
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginId = loginId;
        this.contactEmails = contactEmails;
    }

    @Override
    public void validate() throws ConstraintViolationException {
        super.validate();
        validateLoginIdIsUnique(this.loginId);
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

    public Set<String> getContactEmails() {
        if (contactEmails == null) {
            contactEmails = new HashSet<String>();
        }
        return Collections.unmodifiableSet(contactEmails);
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void updateLoginId(String newLoginId) {
        validateLoginIdIsUnique(newLoginId);

        String oldLoginId = this.loginId;
        this.loginId = newLoginId;

        removeContactEmail(oldLoginId);
        addNewContactEmail(newLoginId);
    }

    public void addNewContactEmail(String email) {
        if (!this.contactEmails.contains(email)) {
            validateContactEmailIsUnique(email);
            this.contactEmails.add(email);
        }
    }

    public void removeContactEmail(String email) {
        this.contactEmails.remove(email);
    }

    private void validateContactEmailIsUnique(String email) {
        UserRepository userRepository = RepositoryRegistry.repository(UserRepository.class);
        if (userRepository.existsByContactEmail(email)) {
            throw new ConstraintViolationException("A user already has this email as a contact email: " + email, null);
        }
    }

    private void validateLoginIdIsUnique(String newLoginId) {
        UserRepository userRepository = RepositoryRegistry.repository(UserRepository.class);
        if (userRepository.existsByLoginId(newLoginId)) {
            throw new ConstraintViolationException("User already exists with login: " + newLoginId, null);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .appendSuper(super.toString())
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("loginId", loginId)
                .append("contactEmails", contactEmails)
                .toString();
    }
}
