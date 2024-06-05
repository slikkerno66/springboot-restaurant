package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminReviewSearchRequestDTO {

    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_13)
    private String restaurantName;

    //This field will resolve to userId (can be more than one)
    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_23)
    private String nameOfUser;

    @Pattern(regexp = "^(DELETED|REJECTED|PENDING|APPROVED)$", message = ErrorBeanConstant.REST_BEAN_24)
    private String reviewStatus;

    @Pattern(regexp = "(?!0000)[0-9]{4}", message = ErrorBeanConstant.REST_BEAN_25)
    private String restaurantZipCode;

    @Pattern(regexp = "^\\d+$", message = ErrorBeanConstant.REST_BEAN_32)
    private String userId;

    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_28)
    private String userState;

    @Pattern(regexp = "(?!0000)[0-9]{4}", message = ErrorBeanConstant.REST_BEAN_26)
    private String userZipCode;

}
