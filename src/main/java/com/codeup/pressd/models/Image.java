package com.codeup.pressd.models;

import javax.persistence.*;

@Entity
@Table(name="images")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 250)
	private String url;

	@Column(name = "delete_url", nullable = false, length = 250)
	private String deleteUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Image() {

	}

	public Image(long id, String url, String deleteUrl, User user) {
		this.id = id;
		this.url = url;
		this.deleteUrl = deleteUrl;
		this.user = user;
	}

	public Image(String url, String deleteUrl, User user) {
		this.url = url;
		this.deleteUrl = deleteUrl;
		this.user = user;
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

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
