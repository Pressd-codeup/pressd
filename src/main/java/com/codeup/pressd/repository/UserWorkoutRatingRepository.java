package com.codeup.pressd.repository;

import com.codeup.pressd.models.User;
import com.codeup.pressd.models.UserWorkoutRating;
import com.codeup.pressd.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWorkoutRatingRepository extends JpaRepository<UserWorkoutRating, Long> {

	List<UserWorkoutRating> getUserWorkoutRatingsByWorkout(Workout workout);

	List<UserWorkoutRating> getUserWorkoutRatingsByWorkoutAndUser(Workout workout, User user);
}
