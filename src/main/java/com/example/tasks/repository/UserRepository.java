package com.example.tasks.repository;

import com.example.tasks.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
       SELECT u
       FROM User u
       WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))
       """)
    List<User> searchByUsername(@Param("username") String username);
}
