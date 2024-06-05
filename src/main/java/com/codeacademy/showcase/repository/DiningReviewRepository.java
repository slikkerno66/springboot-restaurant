package com.codeacademy.showcase.repository;

import com.codeacademy.showcase.entity.DiningReview;
import com.codeacademy.showcase.utilenum.ReviewStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DiningReviewRepository extends CrudRepository<DiningReview, Long> {

    List<DiningReview> findByRestaurantId(Long restaurantId);

    List<DiningReview> findByUserId(Long userId);

    Optional<DiningReview> findByRestaurantIdAndUserId(Long restaurantId, Long userId);

    Optional<DiningReview> findByUserIdAndRestaurantId(Long userId, Long restaurantId);

    List<DiningReview> findByRestaurantIdAndAdminReview(Long restaurantId, ReviewStatus adminReview);

    //Custom query
    //https://stackoverflow.com/questions/68463867/jpa-query-with-both-null-and-non-null-value-for-a-given-query-param
    //https://www.baeldung.com/spring-data-jpa-query
    @Query(value = "SELECT review.* " +
            "FROM DINING_REVIEWS review, RESTAURANTS restaurant, USERS u " +
            "WHERE review.RESTAURANT_ID = restaurant.id " +
            "AND review.USER_ID = u.id " +
            "AND (:restaurantId is null or restaurant.id IN(:restaurantId)) " +
            "AND (:userId is null or u.id IN(:userId)) " +
            "AND (:reviewStatus is null or review.ADMIN_REVIEW = :#{#reviewStatus?.name()})"
            , nativeQuery = true)
    List<DiningReview> customFindReviewByParams(
            List<Long> restaurantId,
            List<Long> userId,
            ReviewStatus reviewStatus);

    @Query(value = "SELECT review.* " +
            "FROM DINING_REVIEWS review, RESTAURANTS restaurant " +
            "WHERE review.RESTAURANT_ID = restaurant.id " +
            "AND (:restaurantId is null or restaurant.id IN(:restaurantId)) " +
            "AND review.ADMIN_REVIEW = 'DELETED'"
            , nativeQuery = true)
    List<DiningReview> customFindDeletedReviewByParams(
            List<Long> restaurantId);

    //    @Query(value = "SELECT review.id as id, review.RESTAURANT_ID as restaurantId, review.USER_ID as userId, u.name as nameOfUser, u.STATE as userState, " +
//            "u.ZIP_CODE as ZIP_CODE, restaurant.name as restaurantName, restaurant.ZIP_CODE as restaurantZipCode, review.PEANUT_SCORE as peanutScore, " +
//            "review.EGG_SCORE as eggScore, review.DAIRY_SCORE as dairyScore, review.COMMENTARY as commentary, review.ADMIN_REVIEW as adminReview " +
//            "FROM DINING_REVIEWS review, RESTAURANTS restaurant, USERS u " +
//            "WHERE review.RESTAURANT_ID = restaurant.id " +
//            "AND review.USER_ID = u.id " +
//            "AND (:restaurantId is null or restaurant.id IN(:restaurantId)) " +
//            "AND (:userId is null or u.id IN(:userId)) " +
//            "AND (:reviewStatus is null or review.ADMIN_REVIEW = :#{#reviewStatus?.name()})"
//            , nativeQuery = true)

//    @Query("SELECT new com.codeacademy.showcase.entity.MergedDiningRestaurantUsers(" +
//            "review.id, review.restaurantId, review.userId, u.name, u.state, u.zipCode, " +
//            "restaurant.name, restaurant.zipCode, review.peanutScore, review.eggScore, review.dairyScore, " +
//            "review.commentary, review.adminReview) " +
//            "FROM DiningReview review " +
//            "JOIN Restaurant restaurant ON review.restaurantId = restaurant.id " +
//            "JOIN Users u ON review.userId = u.id " +
//            "WHERE (:restaurantId is null or restaurant.id IN :restaurantId) " +
//            "AND (:userId is null or u.id IN :userId) " +
//            "AND (:reviewStatus is null or review.adminReview = :#{#reviewStatus?.name()})")
//    MergedDiningRestaurantUsers findReviewByParams(
//            List<Long> restaurantId,
//            List<Long> userId,
//            ReviewStatus reviewStatus);
}
