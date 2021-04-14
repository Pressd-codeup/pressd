package com.codeup.pressd.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ratings")
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToMany(mappedBy = "ratings")
	List<Workout> workouts;

	@ManyToMany(mappedBy = "ratings")
	List<User> users;


	@Column(name = "stars", nullable = false)
	private long stars;

	public Rating() {
	}

	public Rating(long id, List<Workout> workouts, List<User> users, long stars) {
		this.id = id;
		this.workouts = workouts;
		this.users = users;
		this.stars = stars;
	}

	public Rating(List<Workout> workouts, List<User> users, long stars) {
		this.workouts = workouts;
		this.users = users;
		this.stars = stars;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Workout> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(List<Workout> workouts) {
		this.workouts = workouts;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public long getStars() {
		return stars;
	}

	public void setStars(long stars) {
		this.stars = stars;
	}
}
