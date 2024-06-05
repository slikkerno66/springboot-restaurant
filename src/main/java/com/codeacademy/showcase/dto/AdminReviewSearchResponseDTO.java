package com.codeacademy.showcase.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminReviewSearchResponseDTO {

    List<DiningReviewRestaurantUsers> searchResult;

}
