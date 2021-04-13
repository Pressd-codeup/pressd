package com.codeup.pressd.models;

import javax.persistence.*;

@Entity
@Table(name="categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 45, nullable = false)
	private String name;

	public Category() {
	}

	public Category(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Category(String name) {
		this.name = name;
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
}