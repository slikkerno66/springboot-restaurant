package com.codeacademy.showcase.service;

import com.codeacademy.showcase.constant.ErrorCodeConstant;
import com.codeacademy.showcase.dto.CreateUserRequestDTO;
import com.codeacademy.showcase.entity.User;
import com.codeacademy.showcase.exception.RestaurantCustomException;
import com.codeacademy.showcase.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public User createUser(CreateUserRequestDTO requestDTO) {

        User toCreateUser = convertToUserEntity(requestDTO);

        User existingUser = this.userRepository.findByName(toCreateUser.getName());

        if (existingUser == null) {
            return this.userRepository.save(toCreateUser);
        } else {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_1, HttpStatus.BAD_REQUEST);
        }

    }

    private User convertToUserEntity(CreateUserRequestDTO requestDTO) {
        return User.builder()
                .name(requestDTO.getName())
                .state(requestDTO.getState())
                .zipCode(requestDTO.getZipCode())
                .peanutAllergies(requestDTO.getPeanutAllergies())
                .eggAllergies(requestDTO.getEggAllergies())
                .dairyAllergies(requestDTO.getDairyAllergies())
                .build();
    }

}
