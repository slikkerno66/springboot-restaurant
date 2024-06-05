package com.codeacademy.showcase.dto;

import com.codeacademy.showcase.entity.DiningReview;
import com.codeacademy.showcase.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDeleteUserResponseDTO {

    private Users deletedUser;

    private List<DiningReview> deletedDiningReview;

}
