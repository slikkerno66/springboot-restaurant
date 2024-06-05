package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdminNewRestaurantRequestDTO {

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_12)
    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_13)
    private String restaurantName;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_4)
    @Pattern(regexp = "(?!0000)[0-9]{4}", message = ErrorBeanConstant.REST_BEAN_5)
    private String zipCode;

}
