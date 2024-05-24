package com.codeacademy.showcase.repository;

import com.codeacademy.showcase.entity.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    public List<Restaurant> findByNameAndZipCode(String name, String zipCode);

    public List<Restaurant> findByZipCodeAndPeanutScoreGreaterThanEqual(String zipCode, Double peanutScore);

    public List<Restaurant> findByZipCodeAndEggScoreGreaterThanEqual(String zipCode, Double eggScore);

    public List<Restaurant> findByZipCodeAndDairyScoreGreaterThanEqual(String zipCode, Double dairy);
}
