package com.codeup.pressd.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workouts")
public class Workout {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 250, nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String body;

	@Column(name = "date_posted", nullable = false)
	private LocalDateTime datePosted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "workout")
	private List<Comment> comments;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id", nullable = false)
	private Image image;

	@ManyToMany
	@JoinTable(name = "workout_categories", joinColumns = @JoinColumn(name = "workout_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id"))
	List<Category> categories;


	public Workout() {
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Workout(String title, String body, List<Category> categories) {
		this.title = title;
		this.body = body;
		this.categories = categories;
		this.comments = new ArrayList<>();
		this.datePosted = LocalDateTime.now();
	}

	public Workout(long id, String title, String body, LocalDateTime datePosted, User user, List<Comment> comments, List<Category> categories) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.datePosted = datePosted;
		this.user = user;
		this.comments = comments;
		this.categories = categories;

	}

	public Workout(String title, String body, LocalDateTime datePosted, User user, List<Comment> comments, List<Category> categories) {
		this.title = title;
		this.body = body;
		this.datePosted = datePosted;
		this.user = user;
		this.comments = comments;
		this.categories = categories;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}


	public double getRatingAverage(List<Rating> ratings) {

		if (ratings.isEmpty()) return 0.0;
		long sum = 0;
		for (Rating rating : ratings) {
			sum += rating.getStars();
		}
		return (sum * 1.0) / ratings.size();
	}
}
