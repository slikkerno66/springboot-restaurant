package com.codeacademy.showcase.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
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
    private Double peanutScore;

    @Column(name = "EGG_SCORE")
    private Double eggScore;

    @Column(name = "DAIRY_SCORE")
    private Double dairyScore;

    @Column(name = "OVERALL_SCORE")
    private Double overallScore;

}
