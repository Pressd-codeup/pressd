package com.codeup.pressd.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="types")
public class Type {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 45)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
	private List<Post> posts;


	public Type() {
	}

	public Type(long id, String name, List<Post> posts) {
		this.id = id;
		this.name = name;
		this.posts = posts;
	}

	public Type(String name, List<Post> posts) {
		this.name = name;
		this.posts = posts;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
}