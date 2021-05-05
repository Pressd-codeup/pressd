package com.codeup.pressd.models;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 250)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String body;

	@Column(nullable = false)
	private String city;

	@Column(name = "date_posted", nullable = false)
	private LocalDateTime datePosted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", nullable = false)
	private Type type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id", nullable = false)
	private Image image;

	public Post() {
	}

	public Post(String title, String body,
				//long zipcode,
				Type type, Image image, String city) {
		this.title = title;
		this.body = body;
		//this.zipcode = zipcode;
		this.type = type;
		this.datePosted = LocalDateTime.now();
		this.image = image;
		this.city = city;
	}

	public Post(long id, String title, String body,
				//long zipcode,
				LocalDateTime datePosted, Type type, User user, Image image, String city) {
		this.id = id;
		this.title = title;
		this.body = body;
		//this.zipcode = zipcode;
		this.datePosted = datePosted;
		this.type = type;
		this.user = user;
		this.image = image;
		this.city = city;
	}

	public Post(String title, String body,
				//long zipcode,
				LocalDateTime datePosted, Type type, User user, Image image, String city) {
		this.title = title;
		this.body = body;
		//this.zipcode = zipcode;
		this.datePosted = datePosted;
		this.type = type;
		this.user = user;
		this.image = image;
		this.city = city;
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

	/*public long getZipcode() {
		return zipcode;
	}

	public void setZipcode(long zipcode) {
		this.zipcode = zipcode;
	}*/

	public LocalDateTime getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(LocalDateTime datePosted) {
		this.datePosted = datePosted;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
