package com.codeacademy.showcase.controller;

import com.codeacademy.showcase.dto.CreateUserRequestDTO;
import com.codeacademy.showcase.entity.User;
import com.codeacademy.showcase.exception.RestaurantCustomException;
import com.codeacademy.showcase.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User registerUser(@Valid @RequestBody CreateUserRequestDTO requestDTO) {

        return userService.createUser(requestDTO);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
//        return null;
//    }

}
