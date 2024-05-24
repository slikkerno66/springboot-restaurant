package com.codeacademy.showcase.repository;

import com.codeacademy.showcase.entity.DiningReview;
import com.codeacademy.showcase.utilenum.ReviewStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiningReviewRepository extends CrudRepository<DiningReview, Long> {

    List<DiningReview> findByAdminReview(ReviewStatus reviewStatus);

    List<DiningReview> findByRestaurantIdAndAdminReview(Long restaurantId, ReviewStatus adminReview);
}
