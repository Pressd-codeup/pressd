package com.codeup.pressd.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 50, unique = true)
	private String username;

	@Column(nullable = false, length = 50, unique = true)
	private String email;

	@Column(nullable = false, length = 250)
	private String password;

	@Column(name = "is_coach", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isCoach;

	@Column(name = "is_admin", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isAdmin;

	@Column(name = "date_joined", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateJoined;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String about;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Post> posts;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Workout> workouts;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Comment> comments;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Rating> ratings;


	public User() {
	}

	public User(long id, String username, String email, String password, boolean isCoach, boolean isAdmin, LocalDateTime dateJoined, String about, List<Post> posts, List<Workout> workouts, List<Comment> comments, List<Rating> ratings) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.isCoach = isCoach;
		this.isAdmin = isAdmin;
		this.dateJoined = dateJoined;
		this.about = about;
		this.posts = posts;
		this.workouts = workouts;
		this.comments = comments;
		this.ratings = ratings;
	}

	public User(String username, String email, String password, boolean isCoach, boolean isAdmin, LocalDateTime dateJoined, String about, List<Post> posts, List<Workout> workouts, List<Comment> comments, List<Rating> ratings) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.isCoach = isCoach;
		this.isAdmin = isAdmin;
		this.dateJoined = dateJoined;
		this.about = about;
		this.posts = posts;
		this.workouts = workouts;
		this.comments = comments;
		this.ratings = ratings;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isCoach() {
		return isCoach;
	}

	public void setCoach(boolean coach) {
		isCoach = coach;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}

	public LocalDateTime getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(LocalDateTime dateJoined) {
		this.dateJoined = dateJoined;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Workout> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(List<Workout> workouts) {
		this.workouts = workouts;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
}
