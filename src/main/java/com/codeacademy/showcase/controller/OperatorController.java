package com.codeacademy.showcase.controller;

import com.codeacademy.showcase.dto.AdminReviewSearchRequestDTO;
import com.codeacademy.showcase.dto.AdminReviewSearchResponseDTO;
import com.codeacademy.showcase.dto.OperatorUsersSearchRequestDTO;
import com.codeacademy.showcase.entity.Users;
import com.codeacademy.showcase.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    private OperatorService operatorService;

    @GetMapping("/review")
    public AdminReviewSearchResponseDTO getReview(@Validated @ModelAttribute AdminReviewSearchRequestDTO requestDTO) {
        return operatorService.getDiningReview(requestDTO);
    }

    @GetMapping("/users")
    public List<Users> getUsers(@Validated @ModelAttribute OperatorUsersSearchRequestDTO requestDTO) {
        return operatorService.getUsers(requestDTO);
    }

}
