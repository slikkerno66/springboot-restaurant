package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateUserRequestDTO {

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_6)
    @Size(max = 60, message = ErrorBeanConstant.REST_BEAN_7)
    private String username;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_10)
    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_11)
    private String password;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_1)
    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_2)
    private String name;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_3)
    private String state;

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_4)
    @Pattern(regexp = "(?!0000)[0-9]{4}", message = ErrorBeanConstant.REST_BEAN_5)
    private String zipCode;

    @Pattern(regexp = "^(ROLE_REGISTERED|ROLE_OPERATOR|ROLE_ADMIN)$"
            , message = ErrorBeanConstant.REST_BEAN_9)
    private String role;

    private Boolean peanutAllergies;

    private Boolean eggAllergies;

    private Boolean dairyAllergies;
}
