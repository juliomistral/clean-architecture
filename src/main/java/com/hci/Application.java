package com.hci;


import com.hci.common.dao.RepositoryRegistry;
import com.hci.common.domain.Id;
import com.hci.common.domain.UuidId;
import com.hci.dao.UserRepository;
import com.hci.domain.User;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Application {

    private void run(String[] args) {
        UserRepository repo = RepositoryRegistry.repository(UserRepository.class);

        // Create new VALID user
        User user = new User("Julio", "Mistral", "julio.mistral@gmail.com", null);
        user.validate();
        repo.create(user);
        System.out.println("User created: " + user.toString());

        // Create INVALID user: invalid login ID
        try {
            User otherUser = new User("Julio", "Mistral", "julio.mistral", null);
            otherUser.validate();
            repo.create(otherUser);
        } catch (ConstraintViolationException cve) {
            printViolations(cve);
        }
    }

    private void printViolations(ConstraintViolationException cve) {
        System.out.println("Error occurred during validation: ");
        for (ConstraintViolation violation : cve.getConstraintViolations()) {
            System.out.println("\t" + violation.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        RepositoryRegistry.registerRepository(UserRepository.class, new Application.InMemoryUserRespository());

        Application app = new Application();
        app.run(args);
    }

    private static class InMemoryUserRespository implements UserRepository {
        private Map<Id<User>, User> lookupById;
        private Map<String, User> lookupByLoginId;
        private Map<String, User> lookupByContactId;


        public InMemoryUserRespository() {
            this.lookupById = new HashMap<Id<User>, User>();
            this.lookupByLoginId = new HashMap<String, User>();
            this.lookupByContactId = new HashMap<String, User>();
        }

        @Override
        public Id<User> create(User user) {
            String rawUuid = UUID.randomUUID().toString();
            Id<User> newId = new UuidId<User>(rawUuid, User.class);
            user.id(newId);

            lookupById.put(newId, user);
            lookupByLoginId.put(user.getLoginId(), user);
            for (String contactEmail : user.getContactEmails()) {
                lookupByContactId.put(contactEmail, user);
            }

            return newId;
        }

        @Override
        public User findByLoginId(String loginId) {
            return lookupByLoginId.get(loginId);
        }

        @Override
        public boolean existsByLoginId(String loginId) {
            return lookupByLoginId.containsKey(loginId);
        }

        @Override
        public boolean existsByContactEmail(String email) {
            return lookupByContactId.containsKey(email);
        }

        @Override
        public User get(Id<User> id) {
            return lookupById.get(id);
        }
    }
}