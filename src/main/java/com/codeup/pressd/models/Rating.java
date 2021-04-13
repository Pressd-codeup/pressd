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
	private long stars;

	public Rating() {
	}

	public Rating(long id, Workout workout, User user, long stars) {
		this.id = id;
		this.workout = workout;
		this.user = user;
		this.stars = stars;
	}

	public Rating(Workout workout, User user, long stars) {
		this.workout = workout;
		this.user = user;
		this.stars = stars;
	}

	public long getStars() {
		return stars;
	}

	public void setStars(long stars) {
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
