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

	UserWorkoutRating getUserWorkoutRatingByWorkoutAndUser(Workout workout, User user);

	default double getAverageRating(List<UserWorkoutRating> list) {
		if (list.isEmpty()) return 0.0;
		long sum = 0L;
		for (UserWorkoutRating uwr : list) {
			sum += uwr.getRating().getStars();
		}
		return (1.0 * sum) / list.size();
	}
}
