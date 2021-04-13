package com.codeup.pressd.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@Column(columnDefinition = "TEXT", length = 3000, nullable = false)
	private String body;

	@Column(name = "date_posted", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateJoined;

	@ManyToOne()
	@JoinColumn(name = "workout_id")
	private Workout workout;

	@ManyToOne()
	@JoinColumn(name = "user_id")
	private User user;


	public Comment() {
	}

	public Comment(String body, User user) {
		this.body = body;
		this.user = user;
	}

	public Comment(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
