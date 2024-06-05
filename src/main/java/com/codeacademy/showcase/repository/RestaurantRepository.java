package com.codeacademy.showcase.repository;

import com.codeacademy.showcase.entity.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    public Optional<Restaurant> findByName(String name);

    public List<Restaurant> findByZipCode(String zipCode);

    public List<Restaurant> findByNameAndZipCode(String name, String zipCode);

    public List<Restaurant> findByZipCodeAndPeanutScoreGreaterThanEqual(String zipCode, Double peanutScore);

    public List<Restaurant> findByZipCodeAndEggScoreGreaterThanEqual(String zipCode, Double eggScore);

    public List<Restaurant> findByZipCodeAndDairyScoreGreaterThanEqual(String zipCode, Double dairy);

    //Custom query
    @Query(value = "SELECT restaurant.* FROM RESTAURANTS restaurant " +
            "WHERE (:name IS NULL OR restaurant.NAME = :name) " +
            "AND (:zipCode IS NULL OR restaurant.ZIP_CODE = :zipCode)", nativeQuery = true)
    public List<Restaurant> customFindByNameAndZipCodeWithNullableValues(String name, String zipCode);
}
