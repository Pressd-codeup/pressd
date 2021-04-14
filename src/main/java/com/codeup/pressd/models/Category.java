package com.codeup.pressd.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 45, nullable = false)
	private String name;

	@ManyToMany(mappedBy = "categories")
	List<Workout> workout;

	public Category() {
	}

	public Category(long id, String name, List<Workout> workout) {
		this.id = id;
		this.name = name;
		this.workout = workout;
	}

	public Category(String name, List<Workout> workout) {
		this.name = name;
		this.workout = workout;
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

	public List<Workout> getWorkout() {
		return workout;
	}

	public void setWorkout(List<Workout> workout) {
		this.workout = workout;
	}
}