package com.codeacademy.showcase.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Data
public class UserReviewResponseDTO {

    private String restaurantName;

    private String submittedNameOfUser;

    private String zipCode;

    private BigDecimal peanutScore;

    private BigDecimal eggScore;

    private BigDecimal dairyScore;

    private BigDecimal overallScore;

    private String commentary;

    private String userReviewTime;

    private String adminReviewTime;

    private String reviewStatus;
}
