package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminApprovalRequestDTO {

    @Pattern(regexp = "^\\d+$", message = ErrorBeanConstant.REST_BEAN_32)
    @NotNull(message = ErrorBeanConstant.REST_BEAN_30)
    private String userId;

    @Pattern(regexp = "^\\d+$", message = ErrorBeanConstant.REST_BEAN_33)
    @NotNull(message = ErrorBeanConstant.REST_BEAN_31)
    private String restaurantId;

    @NotNull(message = ErrorBeanConstant.REST_BEAN_29)
    private Boolean reviewAccept;

}
