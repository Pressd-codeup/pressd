package com.codeup.pressd.repository;


import com.codeup.pressd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
