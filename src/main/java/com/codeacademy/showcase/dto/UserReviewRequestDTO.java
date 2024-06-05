package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.constant.ErrorBeanConstant;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@Builder
@Data
public class UserReviewRequestDTO {

    @NotBlank(message = ErrorBeanConstant.REST_BEAN_12)
    @Size(max = 255, message = ErrorBeanConstant.REST_BEAN_13)
    private String restaurantName;

    @NotNull(message = ErrorBeanConstant.REST_BEAN_14)
    @DecimalMin(value = "0.0", message = ErrorBeanConstant.REST_BEAN_15)
    @DecimalMax(value = "10.0", message = ErrorBeanConstant.REST_BEAN_15)
    private BigDecimal peanutScore;

    @NotNull(message = ErrorBeanConstant.REST_BEAN_16)
    @DecimalMin(value = "0.0", message = ErrorBeanConstant.REST_BEAN_17)
    @DecimalMax(value = "10.0", message = ErrorBeanConstant.REST_BEAN_17)
    private BigDecimal eggScore;

    @NotNull(message = ErrorBeanConstant.REST_BEAN_18)
    @DecimalMin(value = "0.0", message = ErrorBeanConstant.REST_BEAN_19)
    @DecimalMax(value = "10.0", message = ErrorBeanConstant.REST_BEAN_19)
    private BigDecimal dairyScore;

    @NotNull(message = ErrorBeanConstant.REST_BEAN_20)
    @DecimalMin(value = "0.0", message = ErrorBeanConstant.REST_BEAN_21)
    @DecimalMax(value = "10.0", message = ErrorBeanConstant.REST_BEAN_21)
    private BigDecimal overallScore;

    @Size(min = 0, max = 500, message = ErrorBeanConstant.REST_BEAN_22)
    private String commentary;
}
