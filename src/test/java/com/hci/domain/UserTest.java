package com.hci.domain;

import com.hci.common.dao.RepositoryRegistry;
import com.hci.dao.UserRepository;
import static com.hci.test.matchers.ConstraintViolationsMatcher.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

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
        User user = new User("first", "last", "julio", null);

        // then: a constraint exception is thrown
        thrown.expect(hasViolation("loginId", "Login ID must be an email.  Invalid value = julio"));

        // when: the user is validated
        user.validate();
    }

    @Test
    public void shouldFailValidationIfCreatingAUserWithAnExistingEmail() {
        // given: an existing user with a login ID
        doReturn(true).when(userRepository).existsByLoginId("email@domain.com");

        // and: a user is being created with the same email
        User user = new User("fist", "last", "email@domain.com", null);

        // then: a constraint exception is thrown
        thrown.expect(isViolationException("User already exists with login: email@domain.com"));

        // when: the user is validated
        user.validate();
    }

}