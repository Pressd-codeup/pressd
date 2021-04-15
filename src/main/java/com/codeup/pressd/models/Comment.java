package com.codeup.pressd.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;


	@Column(length = 500, nullable = false)
	private String body;

	@Column(name = "date_posted", nullable = false)
	private LocalDateTime datePosted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workout_id", nullable = false)
	private Workout workout;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;


	public Comment() {
	}

	public Comment(long id, String body, LocalDateTime datePosted, Workout workout, User user, Post postId) {
		this.id = id;
		this.body = body;
		this.datePosted = datePosted;
		this.workout = workout;
		this.user = user;
	}

	public Comment(String body, LocalDateTime datePosted, Workout workout, User user) {
		this.body = body;
		this.datePosted = datePosted;
		this.workout = workout;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public LocalDateTime getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(LocalDateTime datePosted) {
		this.datePosted = datePosted;
	}

	public Workout getWorkout() {
		return workout;
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
