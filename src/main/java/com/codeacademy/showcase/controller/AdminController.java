package com.codeacademy.showcase.controller;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import com.codeacademy.showcase.dto.*;
import com.codeacademy.showcase.entity.DiningReview;
import com.codeacademy.showcase.entity.Restaurant;
import com.codeacademy.showcase.service.AdminService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/review")
    public DiningReview updateReviewStatus(@Validated @RequestBody AdminApprovalRequestDTO requestDTO) {
        return adminService.updateReviewStatus(requestDTO);
    }

    @PostMapping("/restaurant")
    public Restaurant postNewRestaurant(@Validated @RequestBody AdminNewRestaurantRequestDTO requestDTO) {
        return adminService.createNewRestaurant(requestDTO);
    }

    @DeleteMapping("/restaurant")
    public AdminDeleteRestaurantResponseDTO deleteRestaurant(@Validated @RequestBody AdminDeleteRestaurantRequestDTO requestDTO) {
        return adminService.removeRestaurant(requestDTO);
    }

    @DeleteMapping("/user")
    public AdminDeleteUserResponseDTO deleteUser(@RequestParam
                                                 @Pattern(regexp = "^\\d+$", message = ErrorBeanConstant.REST_BEAN_32)
                                                 String id) {
        return adminService.removeUser(id);
    }

}
