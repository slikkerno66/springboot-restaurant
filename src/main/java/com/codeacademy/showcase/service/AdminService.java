package com.codeacademy.showcase.service;

import com.codeacademy.showcase.constant.ErrorCodeConstant;
import com.codeacademy.showcase.dto.*;
import com.codeacademy.showcase.entity.DiningReview;
import com.codeacademy.showcase.entity.Restaurant;
import com.codeacademy.showcase.entity.Users;
import com.codeacademy.showcase.exception.RestaurantCustomException;
import com.codeacademy.showcase.repository.DiningReviewRepository;
import com.codeacademy.showcase.repository.RestaurantRepository;
import com.codeacademy.showcase.repository.UserRepository;
import com.codeacademy.showcase.utilenum.ReviewStatus;
import com.codeacademy.showcase.utils.UserAuthorityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DiningReviewRepository diningReviewRepository;

    public List<Users> getAllUserInSystem() {
        Iterable<Users> allUser = userRepository.findAll();
        return StreamSupport.stream(allUser.spliterator(), false)
                .collect(Collectors.toList());
    }

    public DiningReview updateReviewStatus(AdminApprovalRequestDTO requestDTO) {

        Long userId = Long.valueOf(requestDTO.getUserId());
        Long restaurantId = Long.valueOf(requestDTO.getRestaurantId());

        Optional<Users> usersOptional = userRepository.findById(userId);

        if (usersOptional.isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_17, HttpStatus.BAD_REQUEST);
        }

        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_18, HttpStatus.BAD_REQUEST);
        }

        Optional<DiningReview> diningReviewOptional = diningReviewRepository.findByRestaurantIdAndUserId(restaurantId, userId);
        if (diningReviewOptional.isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_19, HttpStatus.BAD_REQUEST);
        }

        DiningReview diningReview = diningReviewOptional.get();
        ReviewStatus inputReviewStatus = requestDTO.getReviewAccept() ? ReviewStatus.APPROVED : ReviewStatus.REJECTED;
        diningReview.setAdminReview(inputReviewStatus);
        String timestamp = Instant.now().toString();
        diningReview.setAdminReviewTime(timestamp);

        DiningReview updatedDiningReview = diningReviewRepository.save(diningReview);

        //update restaurant overall score
        updateRestaurantScore(restaurantId, restaurantOptional.get());

        return updatedDiningReview;
    }

    private void updateRestaurantScore(Long restaurantId, Restaurant currentRestaurant) {
        List<DiningReview> diningReviews = diningReviewRepository.findByRestaurantIdAndAdminReview(restaurantId, ReviewStatus.APPROVED);

        BigDecimal diningReviewsSize = new BigDecimal(diningReviews.size());
        BigDecimal newPeanutScore = new BigDecimal("0");
        BigDecimal newEggScore = new BigDecimal("0");
        BigDecimal newDairyScore = new BigDecimal("0");
        BigDecimal newOverallScore = new BigDecimal("0");

        if (!diningReviews.isEmpty()) {
            for (DiningReview diningReview : diningReviews) {
                newPeanutScore = newPeanutScore.add(diningReview.getPeanutScore());
                newEggScore = newEggScore.add(diningReview.getEggScore());
                newDairyScore = newDairyScore.add(diningReview.getDairyScore());
            }

            newPeanutScore = newPeanutScore.divide(diningReviewsSize, RoundingMode.UNNECESSARY);
            newEggScore = newEggScore.divide(diningReviewsSize, RoundingMode.UNNECESSARY);
            newDairyScore = newDairyScore.divide(diningReviewsSize, RoundingMode.UNNECESSARY);

            //Recalculate overall score
            newOverallScore = newOverallScore.add(newPeanutScore);
            newOverallScore = newOverallScore.add(newEggScore);
            newOverallScore = newOverallScore.add(newDairyScore);
            newOverallScore = newOverallScore.divide(new BigDecimal(DiningReview.NUMBER_OF_SCORE_TYPE), RoundingMode.UNNECESSARY);

        }


        currentRestaurant.setPeanutScore(newPeanutScore);
        currentRestaurant.setEggScore(newEggScore);
        currentRestaurant.setDairyScore(newDairyScore);
        currentRestaurant.setOverallScore(newOverallScore);

        restaurantRepository.save(currentRestaurant);
    }

    public Restaurant createNewRestaurant(AdminNewRestaurantRequestDTO requestDTO) {
        // check restaurant name is exist in the system or not (restaurant cannot have the same name)
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName(requestDTO.getRestaurantName());
        if (restaurantOptional.isPresent()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_20, HttpStatus.BAD_REQUEST);
        }

        // Build new instance of Restaurant entity to save into the DB
        Restaurant toSaveRestaurant = Restaurant.builder()
                .name(requestDTO.getRestaurantName())
                .zipCode(requestDTO.getZipCode())
                .build();

        return restaurantRepository.save(toSaveRestaurant);
    }

    @Transactional
    public AdminDeleteRestaurantResponseDTO removeRestaurant(AdminDeleteRestaurantRequestDTO requestDTO) {
        Optional<Restaurant> toDeleteRestaurantOptional = restaurantRepository.findByName(requestDTO.getRestaurantName());
        if (toDeleteRestaurantOptional.isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_4, HttpStatus.BAD_REQUEST);
        }

        Restaurant toDeleteRestaurant = toDeleteRestaurantOptional.get();

        // Delete all restaurant's review first
        List<DiningReview> toDeleteDiningReview = diningReviewRepository.findByRestaurantId(toDeleteRestaurant.getId());

        if (!toDeleteDiningReview.isEmpty()) {
            diningReviewRepository.deleteAll(toDeleteDiningReview);
        }

        // Delete restaurant
        restaurantRepository.deleteById(toDeleteRestaurant.getId());

        return AdminDeleteRestaurantResponseDTO.builder()
                .deletedRestaurantName(toDeleteRestaurant.getName())
                .deletedDiningReviews(toDeleteDiningReview.size())
                .build();

    }

    @Transactional
    public AdminDeleteUserResponseDTO removeUser(String userId) {

        //check user exist
        // must not be yourself
        Optional<Users> toRemoveUsersOptional = userRepository.findById(Long.valueOf(userId));
        if (toRemoveUsersOptional.isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_17, HttpStatus.BAD_REQUEST);
        }
        Users toRemoveUsers = toRemoveUsersOptional.get();

        String loggedInUsername = UserAuthorityUtils.getUsernameFromAuthentication();
        if (loggedInUsername.equals(toRemoveUsers.getUsername())) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_27, HttpStatus.BAD_REQUEST);
        }

        // Retrieve diningReviewList to see dining review of deleting user and update userId to null
        List<DiningReview> diningReviewList = diningReviewRepository.findByUserId(Long.valueOf(userId));
        if (!diningReviewList.isEmpty()) {
            for (DiningReview diningReview : diningReviewList) {
                diningReview.setUserId(null);
                diningReview.setAdminReview(ReviewStatus.DELETED);
                diningReviewRepository.save(diningReview);
            }
        }

        return AdminDeleteUserResponseDTO.builder()
                .deletedUser(toRemoveUsers)
                .deletedDiningReview(diningReviewList)
                .build();
    }


}
