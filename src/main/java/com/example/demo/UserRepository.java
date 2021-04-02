package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User,Long> {
    @Query("SELECT u from User u where u.userName= :username")
    User findByUserName(@Param("username") String username);
    User findByEmail(String email);
    Long countByEmail(String email);
    Long countByUserName(String username);
}
