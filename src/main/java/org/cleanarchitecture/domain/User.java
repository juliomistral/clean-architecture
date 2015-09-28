package org.cleanarchitecture.domain;

import org.cleanarchitecture.common.domain.Entity;
import org.cleanarchitecture.common.domain.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.cleanarchitecture.common.domain.validation.constraints.email.ValuesAreEmail;
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

    @ValuesAreEmail(message = "Contact emails contains an invalid email: ${validatedValue}")
    private Set<String> contactEmails;


    /**
     * General use constructor which fires off validation after all fields are set
     *
     * @param firstName
     * @param lastName
     * @param loginId
     * @param contactEmails
     */
    public User(String firstName, String lastName, String loginId, Set<String> contactEmails) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginId = loginId;
        this.contactEmails = contactEmails;

        validate();
    }

    /**
     * Re-instatiation constructor, primarily to be used by Repositories implementations (ie, RDBMS).  Does NOT
     * fire off validation.
     *
     * @param id
     * @param version
     * @param created
     * @param updated
     * @param firstName
     * @param lastName
     * @param loginId
     * @param contactEmails
     */
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
        validator.validateValue(User.class, "firstName", firstName);

        this.firstName = firstName;
        return this;
    }

    public User setLastName(String lastName) {
        validator.validateValue(User.class, "lastName", firstName);

        this.lastName = lastName;
        return this;
    }

    public void updateLoginId(String newLoginId) throws ConstraintViolationException {
        UserEmailValidation.validateNoCollisionWithLoginId(newLoginId);
        UserEmailValidation.validateNoCollisionWithContactEmails(newLoginId);

        String oldLoginId = this.loginId;
        this.loginId = newLoginId;

        this.contactEmails.remove(oldLoginId);
        this.contactEmails.add(newLoginId);
    }

    public void addNewContactEmail(String email) throws ConstraintViolationException {
        if (this.contactEmails.contains(email)) {
            UserEmailValidation.validateNoCollisionWithLoginId(email);
            UserEmailValidation.validateNoCollisionWithContactEmails(email);

            this.contactEmails.add(email);
        }
    }

    public void removeContactEmail(String email) throws ConstraintViolationException {
        if (this.contactEmails.contains(email)) {
            this.contactEmails.remove(email);
        }
    }

    @Override
    public void validate() throws ConstraintViolationException {
        super.validate();

        UserEmailValidation.validateNoCollisionWithLoginId(this.loginId);

        for (String email : getContactEmails()) {
            UserEmailValidation.validateNoCollisionWithLoginId(email);
            UserEmailValidation.validateNoCollisionWithContactEmails(email);
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
