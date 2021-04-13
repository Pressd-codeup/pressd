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
	private long zipcode;

	@Column(name = "date_posted", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime datePosted;

	@Column(name = "type_id", nullable = false)
	private long typeId;

	@Column(name = "user_id", nullable = false)
	private long userId;


	public Post() {
	}

	public Post(long id, String title, String body, long zipcode, LocalDateTime datePosted, long typeId, long userId) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.zipcode = zipcode;
		this.datePosted = datePosted;
		this.typeId = typeId;
		this.userId = userId;
	}

	public Post(String title, String body, long zipcode, LocalDateTime datePosted, long typeId, long userId) {
		this.title = title;
		this.body = body;
		this.zipcode = zipcode;
		this.datePosted = datePosted;
		this.typeId = typeId;
		this.userId = userId;
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

	public long getZipcode() {
		return zipcode;
	}

	public void setZipcode(long zipcode) {
		this.zipcode = zipcode;
	}

	public LocalDateTime getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(LocalDateTime datePosted) {
		this.datePosted = datePosted;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
