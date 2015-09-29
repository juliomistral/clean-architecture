package org.cleanarchitecture.domain;

import org.apache.commons.lang3.RandomStringUtils;
import org.cleanarchitecture.common.dao.RepositoryRegistry;
import org.cleanarchitecture.dao.UserRepository;
import org.cleanarchitecture.test.runners.junit.JUnitWithSpecTestNameRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.cleanarchitecture.test.matchers.ConstraintViolationsMatcher.hasViolation;
import static org.cleanarchitecture.test.matchers.ConstraintViolationsMatcher.isViolationException;
import static org.cleanarchitecture.util.CollectionsUtil.asSet;
import static org.mockito.Mockito.doReturn;


@RunWith(JUnitWithSpecTestNameRunner.class)
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
        // then: a constraint exception is thrown
        thrown.expect(hasViolation("loginId", "Login ID cannot be null"));

        // when: a user with a null login ID is created
        User user = new User("first", "last", null, null);
    }

    @Test
    public void shouldFailValidationIfLoginIdIsNotAValidEmail() {
        // then: a constraint exception is thrown
        thrown.expect(hasViolation("loginId", "Login ID must be an email.  Invalid value = julio"));

        // when: a user with a non-email login ID is created
        User user = new User("first", "last", "julio", asSet("julio"));
    }

    @Test
    public void shouldFailValidationIfCreatingAUserWithAnExistingEmail() {
        // given: an existing user with a login ID
        doReturn(true).when(userRepository).existsByLoginId("email@domain.com");

        // then: a constraint exception is thrown
        thrown.expect(isViolationException("Email collides with an existing LOGIN ID: email@domain.com"));

        // when: a user with the same login ID is created
        User user = new User("fist", "last", "email@domain.com", asSet("email@domain.com"));
    }

    @Test
    public void shouldFailIfFirstNameIsLongerThan256Characters() {
        // then: a constraint exception is thrown
        thrown.expect(hasViolation("firstName", "First name cannot be longer than 256 characters long"));

        // when: a new user with too long of a first name is created
        String tooLongFirstName = RandomStringUtils.randomAlphabetic(300);
        User user = new User(tooLongFirstName, "last", "email@domain.com", asSet("email@domain.com"));
    }

    @Test
    public void shouldFailIfLastNameIsLongerThan256Characters() {
        // then: a constraint exception is thrown
        thrown.expect(hasViolation("lastName", "Last name cannot be longer than 256 characters long"));

        // when: a new user with too long of a first name is created
        String tooLongLastName = RandomStringUtils.randomAlphabetic(300);
        User user = new User("first", tooLongLastName, "email@domain.com", asSet("email@domain.com"));
    }

    @Test
    public void shouldFailIfContactsEmailContainsANonEmailValue() {
        // then: a constraint exception is thrown
        thrown.expect(hasViolation("contactEmails", "Contact emails contains an invalid email: [foo]"));

        // when: a new user with a non-email contact email is created
        User user = new User("first", "last", "email@domain.com", asSet("foo"));
    }

    @Test
    public void shouldFailIfCreatingAUserWithAnExistingContactEmail() {
        // given: a user exists with a contact email
        doReturn(true).when(userRepository).existsByContactEmail("email@domain.com");

        // then: a constraint exception is thrown
        thrown.expect(isViolationException("Email collides with an existing LOGIN ID: email@domain.com"));

        // when: a new user with a contact email matching an existing contact email is created
        User user = new User("first", "last", "email@domain.com", asSet("email@domain.com"));
    }

    @Test
    public void shouldFailIfCreatingAUserWithAContactEmailThatConflictsWithAnExistingLoginId() {
        // given: a user exists with a login ID
        doReturn(true).when(userRepository).existsByLoginId("email@domain.com");

        // then: a constraint exception is thrown
        thrown.expect(isViolationException("Email collides with an existing LOGIN ID: email@domain.com"));

        // when: a new user with a contact email matching an existing login ID is created
        User user = new User("first", "last", "other@domain.com", asSet("email@domain.com"));
    }
}