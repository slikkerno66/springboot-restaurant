package com.codeacademy.showcase.entity;

import com.codeacademy.showcase.utilenum.ReviewStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DINING_REVIEWS")
public class DiningReview {

    public static final int NUMBER_OF_SCORE_TYPE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "PEANUT_SCORE")
    private BigDecimal peanutScore;

    @Column(name = "EGG_SCORE")
    private BigDecimal eggScore;

    @Column(name = "DAIRY_SCORE")
    private BigDecimal dairyScore;

    @Column(name = "COMMENTARY")
    private String commentary;

    @Column(name = "USER_REVIEW_TIME")
    private String userReviewTime;

    @Column(name = "ADMIN_REVIEW_TIME")
    private String adminReviewTime;

    @Column(name = "ADMIN_REVIEW")
    @Enumerated(value = EnumType.STRING)
    private ReviewStatus adminReview;
}
