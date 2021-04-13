package com.codeup.pressd.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "is_coach")
	private int isCoach;

	@Column(name = "is_admin")
	private int isAdmin;

	@Column(columnDefinition = "TEXT", length = 3000, nullable = false)
	private String about;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Post> posts;

	public User() {
	}

	public User(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public User(Long id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public User(User copy){
		id = copy.id;
		email = copy.email;
		username = copy.username;
		password = copy.password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public int getIsCoach() {
		return isCoach;
	}

	public void setIsCoach(int isCoach) {
		this.isCoach = isCoach;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
}