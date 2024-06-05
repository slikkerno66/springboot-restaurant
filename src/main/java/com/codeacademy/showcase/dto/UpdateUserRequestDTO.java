package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UpdateUserRequestDTO {

    private Long id;

    @Size(max = 60, message = ErrorBeanConstant.REST_BEAN_7)
    private String username;

    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_11)
    private String password;

    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_2)
    private String name;

    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_27)
    private String state;

    @Pattern(regexp = "^(ROLE_REGISTERED|ROLE_OPERATOR|ROLE_ADMIN)$"
            , message = ErrorBeanConstant.REST_BEAN_9)
    private String role;

    @Pattern(regexp = "(?!0000)[0-9]{4}", message = ErrorBeanConstant.REST_BEAN_5)
    private String zipCode;

    private Boolean peanutAllergies;

    private Boolean eggAllergies;

    private Boolean dairyAllergies;
}
