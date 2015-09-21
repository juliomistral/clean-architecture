package com.hci;

import com.hci.common.RepositoryRegistry;
import com.hci.dao.UserRepository;
import com.hci.domain.User;
import com.hci.domain.base.Id;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class MainController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainController.class, args);

        RepositoryRegistry.registerRepository(new UserRepository() {
            @Override
            public User findByLoginId(String loginId) {
                return null;
            }

            @Override
            public User get(Id<User> id) {
                return null;
            }
        });

        UserRepository userRepository = RepositoryRegistry.repository(UserRepository.class);
    }
}