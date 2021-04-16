package com.codeup.pressd.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_workout_rating")
public class UserWorkoutRating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "workout_id")
	private Workout workout;

	@ManyToOne
	@JoinColumn(name = "rating_id")
	private Rating rating;

	public double getAverageRating(List<UserWorkoutRating> list) {
		if (list.isEmpty()) return 0.0;
		long sum = 0L;
		for (UserWorkoutRating uwr : list) {
			sum += uwr.rating.getStars();
		}
		return (1.0 * sum) / list.size();
	}


}
