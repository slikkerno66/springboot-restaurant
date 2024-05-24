package com.codeacademy.showcase.repository;

import com.codeacademy.showcase.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    public User findByName(String name);

//    @Query("SELECT 1 " +
//            "FROM USER INNER JOIN DINING_REVIEW ON USER.NAME = DINING_REVIEW.NAME_OF_USER " +
//            "WHERE USER.NAME = :name")
//    public List<User> findUserAssociatedWithDiningReview(@Param("name") String name);
}
