package com.project.habit_tracker.Repository;

import com.project.habit_tracker.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    List<User> findAll();
    User findByUsername(String username);
    Optional<User> findOptionalByUsername(String username);
    User findById(int id);
    User findByEmail(String email);
    Optional<User> findOptionalByEmail(String email);
}
