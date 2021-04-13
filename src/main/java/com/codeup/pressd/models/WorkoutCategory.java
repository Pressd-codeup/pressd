package com.codeup.pressd.models;

import javax.persistence.*;

public class WorkoutCategory {

	@JoinColumn(name = "workout_id")
	private Workout workout;

	@JoinColumn(name = "category_id")
	private Category category;

}
