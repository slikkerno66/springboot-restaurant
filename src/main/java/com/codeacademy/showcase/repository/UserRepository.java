package com.codeacademy.showcase.repository;

import com.codeacademy.showcase.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {

    public static final String FIND_BY_USER_INFO = "SELECT u.* FROM USERS u " +
            "WHERE (:username is null or u.USERNAME = :username) " +
            "AND (:role is null or u.ROLE = :role) " +
            "AND (:name is null or u.NAME = :name) " +
            "AND (:state is null or u.STATE = :state) " +
            "AND (:zipCode is null or u.ZIP_CODE = :zipCode) " +
            "AND (:peanutAllergies is null or u.PEANUT_ALLERGIES = :peanutAllergies) " +
            "AND (:eggAllergies is null or u.EGG_ALLERGIES = :eggAllergies) " +
            "AND (:dairyAllergies is null or u.DAIRY_ALLERGIES = :dairyAllergies) ";

    public List<Users> findByName(String name);

    public Optional<Users> findByUsername(String username);

    public List<Users> findByState(String state);

    public List<Users> findByZipCode(String zipCode);

    //Custom query
    @Query(value = "SELECT u.* FROM USERS u " +
            "WHERE (:userId is null or u.id = :userId) " +
            "AND (:nameOfUser is null or u.NAME = :nameOfUser) " +
            "AND (:userState is null or u.STATE = :userState) " +
            "AND (:userZipCode is null or u.ZIP_CODE = :userZipCode)", nativeQuery = true)
    public List<Users> customFindByUserIdAndUserStateAndUserZipCodeWithNullableValues(String userId, String nameOfUser, String userState, String userZipCode);

    @Query(value = FIND_BY_USER_INFO + "AND u.ROLE <> 'ROLE_ADMIN'", nativeQuery = true)
    public List<Users> customFindByUserInfoAsOperator(String username,
                                                      String role,
                                                      String name,
                                                      String state,
                                                      String zipCode,
                                                      Boolean peanutAllergies,
                                                      Boolean eggAllergies,
                                                      Boolean dairyAllergies);

    @Query(value = FIND_BY_USER_INFO, nativeQuery = true)
    public List<Users> customFindByUserInfoAsAdmin(String username,
                                                   String role,
                                                   String name,
                                                   String state,
                                                   String zipCode,
                                                   Boolean peanutAllergies,
                                                   Boolean eggAllergies,
                                                   Boolean dairyAllergies);
}
