package com.codeup.pressd.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="images")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 250)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "image")
	private List<Workout> workouts;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "image")
	private List<Post> posts;

	public Image() {

	}

	public Image(long id, String url, User user, List<Workout> workouts, List<Post> posts) {
		this.id = id;
		this.url = url;
		this.user = user;
		this.workouts = workouts;
		this.posts = posts;
	}

	public Image(String url, User user, List<Workout> workouts, List<Post> posts) {
		this.url = url;
		this.user = user;
		this.workouts = workouts;
		this.posts = posts;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Workout> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(List<Workout> workouts) {
		this.workouts = workouts;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
}
