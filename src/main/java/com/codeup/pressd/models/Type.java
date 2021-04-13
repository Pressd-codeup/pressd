package com.codeup.pressd.models;

import javax.persistence.*;

@Entity
@Table(name="types")
public class Type {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 45)
	private String name;

	public Type() {
	}

	public Type(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Type(String name) {
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