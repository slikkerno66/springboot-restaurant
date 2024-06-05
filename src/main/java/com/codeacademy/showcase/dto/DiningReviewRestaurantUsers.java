package com.codeacademy.showcase.dto;


import com.codeacademy.showcase.utilenum.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiningReviewRestaurantUsers {

    private Long id;

    private Long restaurantId;

    private Long userId;

    private String nameOfUser;

    private String userState;

    private String userZipCode;

    private String restaurantName;

    private String restaurantZipCode;

    private BigDecimal peanutScore;

    private BigDecimal eggScore;

    private BigDecimal dairyScore;

    private String commentary;

    private String userReviewTime;

    private String adminReviewTime;

    private ReviewStatus adminReview;
}
