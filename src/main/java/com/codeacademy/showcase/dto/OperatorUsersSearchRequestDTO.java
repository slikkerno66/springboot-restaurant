package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.utilenum.Role;
import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OperatorUsersSearchRequestDTO {

    @Size(max = 60, message = ErrorBeanConstant.REST_BEAN_7)
    private String username;

    @Pattern(regexp = "^(ROLE_REGISTERED|ROLE_OPERATOR|ROLE_ADMIN)$")
    private String role;

    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_2)
    private String name;

    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_27)
    private String state;

    @Pattern(regexp = "(?!0000)[0-9]{4}", message = ErrorBeanConstant.REST_BEAN_5)
    private String zipCode;

    private Boolean peanutAllergies;

    private Boolean eggAllergies;

    private Boolean dairyAllergies;

}
