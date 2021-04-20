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

	public UserWorkoutRating() {
	}

	public UserWorkoutRating(long id, User user, Workout workout, Rating rating) {
		this.id = id;
		this.user = user;
		this.workout = workout;
		this.rating = rating;
	}

	public UserWorkoutRating(User user, Workout workout, Rating rating) {
		this.user = user;
		this.workout = workout;
		this.rating = rating;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Workout getWorkout() {
		return workout;
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}
}
