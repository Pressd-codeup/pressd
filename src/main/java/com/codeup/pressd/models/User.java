package com.codeup.pressd.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 50, unique = true)
	private String username;

	@Column(nullable = false, length = 50, unique = true)
	private String email;

	@Column(nullable = false, length = 250)
	private String password;

	@Column(name = "is_coach", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isCoach;

	@Column(name = "is_admin", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isAdmin;

	@Column(name = "date_joined", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateJoined;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String about;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Post> posts;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Workout> workouts;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Comment> comments;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Rating> ratings;


	public User() {
	}



}
