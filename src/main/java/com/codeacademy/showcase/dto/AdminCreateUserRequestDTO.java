package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
public class AdminCreateUserRequestDTO {

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_6)
    @Size(max = 60, message = ErrorBeanConstant.REST_BEAN_7)
    private String username;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_7)
    private String password;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_8)
    @Pattern(regexp = "^(REGISTERED|OPERATOR|ADMIN)$"
            , message = ErrorBeanConstant.REST_BEAN_9)
    private String role;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_1)
    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_2)
    private String name;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_3)
    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_27)
    private String state;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_4)
    @Pattern(regexp = "(?!0000)[0-9]{4}", message = ErrorBeanConstant.REST_BEAN_5)
    private String zipCode;

    private Boolean peanutAllergies;

    private Boolean eggAllergies;

    private Boolean dairyAllergies;
}
