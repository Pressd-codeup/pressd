package com.codeup.pressd.controllers;


import com.codeup.pressd.models.Category;
import com.codeup.pressd.models.Workout;
import com.codeup.pressd.repository.CategoryRepository;
import com.codeup.pressd.repository.UserRepository;
import com.codeup.pressd.repository.WorkoutRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class WorkoutController {

	private final WorkoutRepository workoutDao;
	private final UserRepository userDao;
	private final CategoryRepository categoryDao;

	WorkoutController(WorkoutRepository workoutDao, UserRepository userDao, CategoryRepository categoryDao) {
		this.workoutDao = workoutDao;
		this.userDao = userDao;
		this.categoryDao = categoryDao;

	}

	@GetMapping("/workouts")
	public String seeAllWorkouts(Model viewModel) {
		List<Workout> workouts = workoutDao.findAll();
		viewModel.addAttribute("workouts", workouts);
		return "workouts/index";
	}

	@GetMapping("/workouts/category/{id}")
	public String viewWorkoutsByCategory(@PathVariable long id, Model viewModel) {

		Category category = categoryDao.getOne(id);
		List<Workout> workouts = workoutDao.getWorkoutsByCategoriesContaining(category);
		viewModel.addAttribute("workouts", workouts);
		return "workouts/categories";
	}


}
