package com.codeup.pressd.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ratings")
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;



	@Column(name = "stars", nullable = false)
	private long stars;

	public Rating() {
	}

	public Rating(long id, long stars) {
		this.id = id;
		this.stars = stars;
	}

	public Rating(long stars) {

		this.stars = stars;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	public long getStars() {
		return stars;
	}

	public void setStars(long stars) {
		this.stars = stars;
	}
}
