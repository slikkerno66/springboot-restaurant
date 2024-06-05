package com.codeacademy.showcase.controller;

import com.codeacademy.showcase.constant.ErrorCodeConstant;
import com.codeacademy.showcase.entity.Users;
import com.codeacademy.showcase.exception.RestaurantCustomException;
import com.codeacademy.showcase.repository.UserRepository;
import com.codeacademy.showcase.utils.UserAuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public Users home() {

        String username = UserAuthorityUtils.getUsernameFromAuthentication();

        Optional<Users> usersOptional = userRepository.findByUsername(username);

        return usersOptional.orElse(null);
    }

    // spring already provide logout

}
