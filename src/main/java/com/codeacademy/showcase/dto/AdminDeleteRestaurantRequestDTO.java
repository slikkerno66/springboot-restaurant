package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDeleteRestaurantRequestDTO {

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_12)
    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_13)
    private String restaurantName;

}
