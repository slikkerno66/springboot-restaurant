package com.codeacademy.showcase.controller;

import com.codeacademy.showcase.dto.CreateUserRequestDTO;
import com.codeacademy.showcase.dto.UpdateUserRequestDTO;
import com.codeacademy.showcase.dto.UserReviewRequestDTO;
import com.codeacademy.showcase.dto.UserReviewResponseDTO;
import com.codeacademy.showcase.entity.Users;
import com.codeacademy.showcase.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Users getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @PostMapping("/create")
    public Users postNewUser(@Valid @RequestBody CreateUserRequestDTO requestDTO) {
        return userService.createUser(requestDTO);
    }

    @PostMapping("/review")
    public UserReviewResponseDTO postReview(@Valid @RequestBody UserReviewRequestDTO requestDTO) {
        return userService.postReview(requestDTO);
    }

    @PutMapping("/update")
    public Users putUpdateUser(@Valid @RequestBody UpdateUserRequestDTO requestDTO) {
        return userService.updateUser(requestDTO);
    }

}
