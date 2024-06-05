package com.codeacademy.showcase.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdminDeleteRestaurantResponseDTO {

    private String deletedRestaurantName;

    private Integer deletedDiningReviews;

}
