package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateUserRequestDTO {

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_1)
    @Size(max=255, message = ErrorBeanConstant.REST_BEAN_2)
    private String name;

    private String state;

    private String zipCode;

    private Boolean peanutAllergies;

    private Boolean eggAllergies;

    private Boolean dairyAllergies;
}
