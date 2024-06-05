package com.codeacademy.showcase.service;

import com.codeacademy.showcase.constant.ErrorCodeConstant;
import com.codeacademy.showcase.dto.AdminReviewSearchRequestDTO;
import com.codeacademy.showcase.dto.AdminReviewSearchResponseDTO;
import com.codeacademy.showcase.dto.DiningReviewRestaurantUsers;
import com.codeacademy.showcase.dto.OperatorUsersSearchRequestDTO;
import com.codeacademy.showcase.entity.DiningReview;
import com.codeacademy.showcase.entity.Restaurant;
import com.codeacademy.showcase.entity.Users;
import com.codeacademy.showcase.exception.RestaurantCustomException;
import com.codeacademy.showcase.repository.DiningReviewRepository;
import com.codeacademy.showcase.repository.RestaurantRepository;
import com.codeacademy.showcase.repository.UserRepository;
import com.codeacademy.showcase.utilenum.ReviewStatus;
import com.codeacademy.showcase.utilenum.Role;
import com.codeacademy.showcase.utils.UserAuthorityUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class OperatorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DiningReviewRepository diningReviewRepository;

    public List<Users> getUsers(OperatorUsersSearchRequestDTO requestDTO) {

        if (StringUtils.isNotEmpty(requestDTO.getRole()) &&
                requestDTO.getRole().equals(Role.ROLE_ADMIN.name()) &&
                !UserAuthorityUtils.isLoginAs(Role.ROLE_ADMIN.name())
        ) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_21, HttpStatus.FORBIDDEN);
        }

        List<Users> usersList = new ArrayList<>();
        if (UserAuthorityUtils.isLoginAs(Role.ROLE_ADMIN.name())) {
            usersList = userRepository.customFindByUserInfoAsAdmin(
                    requestDTO.getUsername(),
                    requestDTO.getRole(),
                    requestDTO.getName(),
                    requestDTO.getState(),
                    requestDTO.getZipCode(),
                    requestDTO.getPeanutAllergies(),
                    requestDTO.getEggAllergies(),
                    requestDTO.getDairyAllergies()
            );
        } else if (UserAuthorityUtils.isLoginAs(Role.ROLE_OPERATOR.name())) {
            usersList = userRepository.customFindByUserInfoAsOperator(
                    requestDTO.getUsername(),
                    requestDTO.getRole(),
                    requestDTO.getName(),
                    requestDTO.getState(),
                    requestDTO.getZipCode(),
                    requestDTO.getPeanutAllergies(),
                    requestDTO.getEggAllergies(),
                    requestDTO.getDairyAllergies()
            );
        }
        return usersList;
    }

    public AdminReviewSearchResponseDTO getDiningReview(AdminReviewSearchRequestDTO requestDTO) {
        List<Restaurant> restaurantList = new ArrayList<>();

        //1. find restaurantId
        String restaurantName = StringUtils.isNotEmpty(requestDTO.getRestaurantName()) ? requestDTO.getRestaurantName() : null;
        String restaurantZipCode = StringUtils.isNotEmpty(requestDTO.getRestaurantZipCode()) ? requestDTO.getRestaurantZipCode() : null;

        // check if restaurant name is exist
        if (StringUtils.isNotEmpty(restaurantName) && restaurantRepository.findByName(restaurantName).isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_10, HttpStatus.BAD_REQUEST);
        }
        // check if restaurant zip code is exist
        if (StringUtils.isNotEmpty(restaurantZipCode) && restaurantRepository.findByZipCode(restaurantZipCode).isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_11, HttpStatus.BAD_REQUEST);
        }

        restaurantList = restaurantRepository.customFindByNameAndZipCodeWithNullableValues(
                requestDTO.getRestaurantName(),
                requestDTO.getRestaurantZipCode()
        );
        if ((restaurantName != null || restaurantZipCode != null) && CollectionUtils.isEmpty(restaurantList)) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_8, HttpStatus.BAD_REQUEST);
        }
        List<Long> restaurantIdList = restaurantList.stream().map(Restaurant::getId).toList();

        //2. find user
        //   if userState and userZipcode not null, find Users by userState and userZipcode(userState and userZipCode null is ok because I use raw sql)
        //   if userId is not null, continue query
        List<Users> usersList = new ArrayList<>();

        // check if userid, nameOfUser, userState or zipCode exist or not
        if (requestDTO.getUserId() != null && userRepository.findById(Long.valueOf(requestDTO.getUserId())).isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_12, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotEmpty(requestDTO.getNameOfUser()) && userRepository.findByName(requestDTO.getNameOfUser()).isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_13, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotEmpty(requestDTO.getUserState()) && userRepository.findByState(requestDTO.getUserState()).isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_14, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotEmpty(requestDTO.getUserZipCode()) && userRepository.findByZipCode(requestDTO.getUserZipCode()).isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_16, HttpStatus.BAD_REQUEST);
        }

        usersList = userRepository.customFindByUserIdAndUserStateAndUserZipCodeWithNullableValues(
                requestDTO.getUserId(),
                requestDTO.getNameOfUser(),
                requestDTO.getUserState(),
                requestDTO.getUserZipCode()
        );

        if (CollectionUtils.isEmpty(usersList)) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_15, HttpStatus.BAD_REQUEST);
        }

        List<Long> usersIdList = usersList.stream().map(Users::getId).toList();

        ReviewStatus reviewStatus = StringUtils.isNotEmpty(requestDTO.getReviewStatus()) ? ReviewStatus.valueOf(requestDTO.getReviewStatus()) : null;

        List<DiningReview> diningReviewList = diningReviewRepository.customFindReviewByParams(
                restaurantIdList,
                usersIdList,
                reviewStatus
        );

        // find DELETE record
        List<DiningReview> deletedDiningReviewList = new ArrayList<>();
        if (StringUtils.isEmpty(requestDTO.getUserId()) &&
                StringUtils.isEmpty(requestDTO.getNameOfUser()) &&
                StringUtils.isEmpty(requestDTO.getUserZipCode()) &&
                (StringUtils.isEmpty(requestDTO.getReviewStatus()) || requestDTO.getReviewStatus().equals(ReviewStatus.DELETED.name()))
        ) {
            deletedDiningReviewList = diningReviewRepository.customFindDeletedReviewByParams(restaurantIdList);
        }

        List<DiningReviewRestaurantUsers> DiningReviewRestaurantUsersList = convertToDiningReviewRestaurantUsersList(
                diningReviewList,
                restaurantList,
                usersList,
                deletedDiningReviewList
        );

        return AdminReviewSearchResponseDTO
                .builder()
                .searchResult(DiningReviewRestaurantUsersList)
                .build();
    }

    private List<DiningReviewRestaurantUsers> convertToDiningReviewRestaurantUsersList(List<DiningReview> diningReviewList,
                                                                                       List<Restaurant> restaurantList,
                                                                                       List<Users> usersList,
                                                                                       List<DiningReview> deletedDiningReviewList) {
        List<DiningReviewRestaurantUsers> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(diningReviewList)) {
            for (DiningReview diningReview : diningReviewList) {
                DiningReviewRestaurantUsers diningReviewRestaurantUsers = new DiningReviewRestaurantUsers();

                for (Restaurant restaurant : restaurantList) {
                    if (restaurant.getId().longValue() == diningReview.getRestaurantId().longValue()) {
                        diningReviewRestaurantUsers.setRestaurantName(restaurant.getName());
                        diningReviewRestaurantUsers.setRestaurantZipCode(restaurant.getZipCode());
                        break;
                    }
                }

                for (Users user : usersList) {
                    if (user.getId().longValue() == diningReview.getUserId().longValue()) {
                        diningReviewRestaurantUsers.setNameOfUser(user.getName());
                        diningReviewRestaurantUsers.setUserState(user.getState());
                        diningReviewRestaurantUsers.setUserZipCode(user.getZipCode());
                        break;
                    }
                }

                //set diningReview to diningReviewRestaurantUsers and add to the result list
                diningReviewRestaurantUsers.setId(diningReview.getId());
                diningReviewRestaurantUsers.setRestaurantId(diningReview.getRestaurantId());
                diningReviewRestaurantUsers.setUserId(diningReview.getUserId());
                diningReviewRestaurantUsers.setPeanutScore(diningReview.getPeanutScore());
                diningReviewRestaurantUsers.setEggScore(diningReview.getEggScore());
                diningReviewRestaurantUsers.setDairyScore(diningReview.getDairyScore());
                diningReviewRestaurantUsers.setCommentary(diningReview.getCommentary());
                diningReviewRestaurantUsers.setUserReviewTime(diningReview.getUserReviewTime());
                diningReviewRestaurantUsers.setAdminReviewTime(diningReview.getAdminReviewTime());
                diningReviewRestaurantUsers.setAdminReview(diningReview.getAdminReview());

                result.add(diningReviewRestaurantUsers);
            }
        }

        if (!CollectionUtils.isEmpty(deletedDiningReviewList)) {
            for (DiningReview deletedDiningReview : deletedDiningReviewList) {

                DiningReviewRestaurantUsers diningReviewRestaurantUsers = buildDeletedDiningReviewRestaurant(restaurantList, deletedDiningReview);

                result.add(diningReviewRestaurantUsers);
            }
        }

        return result;
    }

    private DiningReviewRestaurantUsers buildDeletedDiningReviewRestaurant(List<Restaurant> restaurantList, DiningReview deletedDiningReview) {
        DiningReviewRestaurantUsers diningReviewRestaurantUsers = new DiningReviewRestaurantUsers();
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getId().longValue() == deletedDiningReview.getRestaurantId().longValue()) {
                diningReviewRestaurantUsers.setRestaurantName(restaurant.getName());
                diningReviewRestaurantUsers.setRestaurantZipCode(restaurant.getZipCode());
                break;
            }
        }

        diningReviewRestaurantUsers.setId(deletedDiningReview.getId());
        diningReviewRestaurantUsers.setRestaurantId(deletedDiningReview.getRestaurantId());
        diningReviewRestaurantUsers.setPeanutScore(deletedDiningReview.getPeanutScore());
        diningReviewRestaurantUsers.setEggScore(deletedDiningReview.getEggScore());
        diningReviewRestaurantUsers.setDairyScore(deletedDiningReview.getDairyScore());
        diningReviewRestaurantUsers.setCommentary(deletedDiningReview.getCommentary());
        diningReviewRestaurantUsers.setUserReviewTime(deletedDiningReview.getUserReviewTime());
        diningReviewRestaurantUsers.setAdminReviewTime(deletedDiningReview.getAdminReviewTime());
        diningReviewRestaurantUsers.setAdminReview(deletedDiningReview.getAdminReview());
        return diningReviewRestaurantUsers;
    }
}
