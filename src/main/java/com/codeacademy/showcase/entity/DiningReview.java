package com.codeacademy.showcase.entity;

import com.codeacademy.showcase.utilenum.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "DINING_REVIEWS")
public class DiningReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;

    @Column(name = "NAME_OF_USER")
    private String nameOfUser;

    @Column(name = "PEANUT_SCORE")
    private Double peanutScore;

    @Column(name = "EGG_SCORE")
    private Double eggScore;

    @Column(name = "DAIRY_SCORE")
    private Double dairyScore;

    @Column(name = "COMMENTARY")
    private String commentary;

    @Column(name = "ADMIN_REVIEW")
    @Enumerated(value = EnumType.STRING)
    private ReviewStatus adminReview;
}
