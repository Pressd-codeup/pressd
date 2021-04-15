package com.codeup.pressd.repository;


import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.User;
import com.codeup.pressd.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User getUserByWorkoutsContains(Workout workout);
}
