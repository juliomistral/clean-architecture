package org.cleanarchitecture.domain;

import org.cleanarchitecture.common.dao.RepositoryRegistry;
import org.cleanarchitecture.dao.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.cleanarchitecture.test.matchers.ConstraintViolationsMatcher.hasViolation;
import static org.cleanarchitecture.test.matchers.ConstraintViolationsMatcher.isViolationException;
import static org.cleanarchitecture.util.CollectionsUtil.asSet;
import static org.mockito.Mockito.doReturn;

public class UserTest {
    @Mock
    UserRepository userRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        RepositoryRegistry.registerRepository(UserRepository.class, userRepository);
    }

    @Test
    public void shouldFailValidationIfLoginIdIsNull() {
        // given: a user with a null login ID
        User user = new User("first", "last", null, null);

        // then: a constraint exception is thrown
        thrown.expect(hasViolation("loginId", "Login ID cannot be null"));

        // when: the user is validated
        user.validate();
    }

    @Test
    public void shouldFailValidationIfLoginIdIsNotAValidEmail() {
        // given: a user with a non-email login ID
        User user = new User("first", "last", "julio", asSet("julio"));

        // then: a constraint exception is thrown
        thrown.expect(hasViolation("loginId", "Login ID must be an email.  Invalid value = julio"));

        // when: the user is validated
        user.validate();
    }

    @Test
    public void shouldFailValidationIfCreatingAUserWithAnExistingEmail() {
        // given: an existing user with a login ID
        doReturn(true).when(userRepository).existsByLoginId("email@domain.com");

        // and: a user is being created with the same login ID
        User user = new User("fist", "last", "email@domain.com", asSet("email@domain.com"));

        // then: a constraint exception is thrown
        thrown.expect(isViolationException("User already exists with login: email@domain.com"));

        // when: the user is validated
        user.validate();
    }

    @Test
    public void shouldFailIfFirstNameIsLongerThan256Characters() {
        // given: a new user with too long of a first name
        String tooLongFirstName = RandomStringUtils.randomAlphabetic(300);
        User user = new User(tooLongFirstName, "last", "email@domain.com", asSet("email@domain.com"));

        // then: a constraint exception is thrown
        thrown.expect(hasViolation("firstName", "First name cannot be longer than 256 characters long"));

        // when: the user is validated
        user.validate();
    }

    @Test
    public void shouldFailIfLasttNameIsLongerThan256Characters() {
        // given: a new user with too long of a first name
        String tooLongLastName = RandomStringUtils.randomAlphabetic(300);
        User user = new User("first", tooLongLastName, "email@domain.com", asSet("email@domain.com"));

        // then: a constraint exception is thrown
        thrown.expect(hasViolation("lastName", "Last name cannot be longer than 256 characters long"));

        // when: the user is validated
        user.validate();
    }

    @Test
    public void shouldFailIfContactsEmailContainsANonEmailValue() {
        // given: a new user with a non-email contact email
        User user = new User("first", "last", "email@domain.com", asSet("foo"));

        // then: a constraint exception is thrown
        thrown.expect(hasViolation("contactEmails", "Contact emails contains an invalid email: [foo]"));

        // when: the user is validated
        user.validate();
    }

    @Test
    public void shouldFailIfCreatingAUserWithAnExistingContactEmail() {
        // given: a user exists with a contact email
        doReturn(true).when(userRepository).existsByContactEmail("email@domain.com");

        // and: a new user with a contact email matching an existing contact email
        User user = new User("first", "last", "email@domain.com", asSet("email@domain.com"));

        // then: a constraint exception is thrown
        thrown.expect(isViolationException("Contact emails contains an email that already exists: email@domain.com"));

        // when: the user is validated
        user.validate();
    }

    @Test
    public void shouldFailIfCreatingAUserWithAContactEmailThatConflictsWithAnExistingLoginId() {
        // given: a user exists with a login ID
        doReturn(true).when(userRepository).existsByLoginId("email@domain.com");

        // and: a new user with a contact email matching an existing contact email
        User user = new User("first", "last", "other@domain.com", asSet("email@domain.com"));

        // then: a constraint exception is thrown
        thrown.expect(isViolationException("Contact emails contains an email that already exists: email@domain.com"));

        // when: the user is validated
        user.validate();
    }

}