package com.codeacademy.showcase.service;

import com.codeacademy.showcase.constant.ErrorCodeConstant;
import com.codeacademy.showcase.dto.CreateUserRequestDTO;
import com.codeacademy.showcase.dto.UpdateUserRequestDTO;
import com.codeacademy.showcase.dto.UserReviewRequestDTO;
import com.codeacademy.showcase.dto.UserReviewResponseDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DiningReviewRepository diningReviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users getCurrentUserInfo() {

        String username = UserAuthorityUtils.getUsernameFromAuthentication();

        Optional<Users> userOptional = userRepository.findByUsername(username);

        return userOptional.orElse(null);
    }

    public Users createUser(CreateUserRequestDTO requestDTO) {

        //registered user must log out before create new user
        if (UserAuthorityUtils.isLoginAs(Role.ROLE_REGISTERED.name())) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_9, HttpStatus.FORBIDDEN);
        }

        // Only admin can specify role
        if (StringUtils.isNotEmpty(requestDTO.getRole()) && !UserAuthorityUtils.isLoginAs(Role.ROLE_ADMIN.name())) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_26, HttpStatus.FORBIDDEN);
        }

        Users toCreateUser = convertToUserEntity(requestDTO);

        Optional<Users> existingUserOptional = this.userRepository.findByUsername(toCreateUser.getUsername());

        if (existingUserOptional.isEmpty()) {
            return this.userRepository.save(toCreateUser);
        } else {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_1, HttpStatus.BAD_REQUEST);
        }

    }

    private Users convertToUserEntity(CreateUserRequestDTO requestDTO) {
        //encrypt password
        String encryptedPassword = passwordEncoder.encode(requestDTO.getPassword());

        requestDTO.setDairyAllergies(requestDTO.getDairyAllergies() != null && requestDTO.getDairyAllergies());
        requestDTO.setPeanutAllergies(requestDTO.getPeanutAllergies() != null && requestDTO.getPeanutAllergies());
        requestDTO.setEggAllergies(requestDTO.getEggAllergies() != null && requestDTO.getEggAllergies());

        String role = UserAuthorityUtils.isLoginAs(Role.ROLE_ADMIN.name()) ? requestDTO.getRole() : Role.ROLE_REGISTERED.name();

        return Users.builder()
                .username(requestDTO.getUsername())
                .password(encryptedPassword)
                .role(role)
                .name(requestDTO.getName())
                .state(requestDTO.getState())
                .zipCode(requestDTO.getZipCode())
                .peanutAllergies(requestDTO.getPeanutAllergies())
                .eggAllergies(requestDTO.getEggAllergies())
                .dairyAllergies(requestDTO.getDairyAllergies())
                .build();
    }

    public Users updateUser(UpdateUserRequestDTO requestDTO) {

        String username;

        if (UserAuthorityUtils.isLoginAs(Role.ROLE_REGISTERED.name())) {
            username = UserAuthorityUtils.getUsernameFromAuthentication();
            if (requestDTO.getId() != null || StringUtils.isNotEmpty(requestDTO.getUsername()) || StringUtils.isNotEmpty(requestDTO.getRole())) {
                throw new RestaurantCustomException(ErrorCodeConstant.REST_23, HttpStatus.BAD_REQUEST);
            }
        } else {

            if (StringUtils.isNotEmpty(requestDTO.getRole()) && UserAuthorityUtils.isLoginAs(Role.ROLE_OPERATOR.name())) {
                throw new RestaurantCustomException(ErrorCodeConstant.REST_25, HttpStatus.BAD_REQUEST);
            }

            if (requestDTO.getId() == null && StringUtils.isEmpty(requestDTO.getUsername())) {
                throw new RestaurantCustomException(ErrorCodeConstant.REST_22, HttpStatus.BAD_REQUEST);
            }

            username = requestDTO.getUsername();
        }

        Optional<Users> existingUser;
        Users users;

        // if id is not null, mean user can be ROLE_ADMIN or ROLE_OPERATOR
        if (requestDTO.getId() == null) {

            existingUser = userRepository.findByUsername(username);
            if (existingUser.isEmpty()) {
                throw new RestaurantCustomException(ErrorCodeConstant.REST_2, HttpStatus.BAD_REQUEST);
            }
            users = existingUser.get();

            if (isOperatorTryingToUpdateAdmin(users)) {
                throw new RestaurantCustomException(ErrorCodeConstant.REST_2, HttpStatus.BAD_REQUEST);
            }

        } else {
            existingUser = userRepository.findById(requestDTO.getId());

            if (existingUser.isEmpty()) {
                throw new RestaurantCustomException(ErrorCodeConstant.REST_24, HttpStatus.BAD_REQUEST);
            }

            users = existingUser.get();
            if (isOperatorTryingToUpdateAdmin(users)) {
                throw new RestaurantCustomException(ErrorCodeConstant.REST_24, HttpStatus.BAD_REQUEST);
            }

            if (StringUtils.isNotEmpty(requestDTO.getUsername()) && userRepository.findByUsername(username).isPresent()) {
                throw new RestaurantCustomException(ErrorCodeConstant.REST_1, HttpStatus.BAD_REQUEST);
            }
        }

        // update part
        if (StringUtils.isNotEmpty(requestDTO.getUsername())) {
            users.setUsername(requestDTO.getUsername());
        }

        if (StringUtils.isNotEmpty(requestDTO.getRole())) {
            users.setRole(requestDTO.getRole());
        }

        if (StringUtils.isNotEmpty(requestDTO.getPassword())) {
            users.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }
        if (StringUtils.isNotEmpty((requestDTO.getName()))) {
            users.setName(requestDTO.getName());
        }
        if (StringUtils.isNotEmpty(requestDTO.getState())) {
            users.setState(requestDTO.getState());
        }
        if (StringUtils.isNotEmpty(requestDTO.getZipCode())) {
            users.setZipCode(requestDTO.getZipCode());
        }
        if (requestDTO.getPeanutAllergies()) {
            users.setPeanutAllergies(true);
        }
        if (requestDTO.getEggAllergies()) {
            users.setEggAllergies(true);
        }
        if (requestDTO.getDairyAllergies()) {
            users.setDairyAllergies(true);
        }

        return userRepository.save(users);
    }

    private boolean isOperatorTryingToUpdateAdmin(Users users) {
        return users.getRole().equals(Role.ROLE_ADMIN.name()) &&
                UserAuthorityUtils.isLoginAs(Role.ROLE_OPERATOR.name());
    }

    public UserReviewResponseDTO postReview(UserReviewRequestDTO requestDTO) {
        //1. get current user info
        //if it is admin, should not allow to
        if (UserAuthorityUtils.isLoginAs(Role.ROLE_ADMIN.name()) || UserAuthorityUtils.isLoginAs(Role.ROLE_OPERATOR.name())) {
            // only user can write the review
            throw new RestaurantCustomException(ErrorCodeConstant.REST_5, HttpStatus.FORBIDDEN);
        }

        String username = UserAuthorityUtils.getUsernameFromAuthentication();

        //2. find if restaurant is existed
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName(requestDTO.getRestaurantName());
        if (restaurantOptional.isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_4, HttpStatus.BAD_REQUEST);
        }

        Restaurant toReviewRestaurant = restaurantOptional.get();

        //3. get existing user info
        Optional<Users> currentUsersOptional = userRepository.findByUsername(username);

        if (currentUsersOptional.isEmpty()) {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_6, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Users currentUsers = currentUsersOptional.get();

        //4. find if user already review this restaurant
        Optional<DiningReview> diningReviewOptional = diningReviewRepository.findByUserIdAndRestaurantId(currentUsers.getId(), toReviewRestaurant.getId());
        if (diningReviewOptional.isPresent()) {
            DiningReview diningReview = diningReviewOptional.get();
            if (diningReview.getRestaurantId().longValue() == toReviewRestaurant.getId().longValue()) {
                throw new RestaurantCustomException(ErrorCodeConstant.REST_7, HttpStatus.BAD_REQUEST);
            }

        }

        //5. update restaurant review score
        String reviewTime = Instant.now().toString();
        DiningReview toAddDiningReview = DiningReview
                .builder()
                .restaurantId(toReviewRestaurant.getId())
                .userId(currentUsers.getId())
                .peanutScore(requestDTO.getPeanutScore())
                .eggScore(requestDTO.getEggScore())
                .dairyScore(requestDTO.getDairyScore())
                .commentary(requestDTO.getCommentary())
                .userReviewTime(reviewTime)
                .adminReview(ReviewStatus.PENDING)
                .build();

        DiningReview addedDiningReview = diningReviewRepository.save(toAddDiningReview);

        //Restaurant score should now be updated this time since the review hasn't yet approved.
        return UserReviewResponseDTO
                .builder()
                .restaurantName(toReviewRestaurant.getName())
                .submittedNameOfUser(currentUsers.getName())
                .zipCode(toReviewRestaurant.getZipCode())
                .peanutScore(addedDiningReview.getPeanutScore())
                .eggScore(addedDiningReview.getEggScore())
                .dairyScore(addedDiningReview.getDairyScore())
                .overallScore(requestDTO.getOverallScore())
                .commentary(addedDiningReview.getCommentary())
                .userReviewTime(addedDiningReview.getUserReviewTime())
                .adminReviewTime(addedDiningReview.getAdminReviewTime())
                .reviewStatus(addedDiningReview.getAdminReview().name())
                .build();
    }

}
