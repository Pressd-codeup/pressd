package com.codeup.pressd.models;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne()
	@JoinColumn(name = "workout_id")
	private Workout workout;

	@OneToOne()
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "stars")
	private int stars;

	public Rating() {
	}

	public Rating(Workout workout, User user, int stars) {
		this.workout = workout;
		this.user = user;
		this.stars = stars;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
