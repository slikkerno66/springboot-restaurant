package com.codeacademy.showcase.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RESTAURANTS")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "PEANUT_SCORE")
    private BigDecimal peanutScore;

    @Column(name = "EGG_SCORE")
    private BigDecimal eggScore;

    @Column(name = "DAIRY_SCORE")
    private BigDecimal dairyScore;

    @Column(name = "OVERALL_SCORE")
    private BigDecimal overallScore;

}
