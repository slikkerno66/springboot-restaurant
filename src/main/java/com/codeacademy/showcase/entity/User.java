package com.codeacademy.showcase.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "PEANUT_ALLERGIES")
    private Boolean peanutAllergies;

    @Column(name = "EGG_ALLERGIES")
    private Boolean eggAllergies;

    @Column(name = "DAIRY_ALLERGIES")
    private Boolean dairyAllergies;

}
