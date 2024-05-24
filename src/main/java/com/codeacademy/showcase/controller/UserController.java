package com.codeacademy.showcase.controller;

import com.codeacademy.showcase.entity.User;
import com.codeacademy.showcase.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    public UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public User registerUser(@RequestBody User user) {
        if (StringUtils.isNotEmpty(user.getName())) {
            User existingUser = this.userRepository.findByName(user.getName());
            if(existingUser == null) {
                return this.userRepository.save(user);
            } else {
                throw new
            }
        }
        return null;
    }

}
