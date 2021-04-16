package com.codeup.pressd.controllers;


import com.codeup.pressd.models.Category;
import com.codeup.pressd.models.User;
import com.codeup.pressd.models.Workout;
import com.codeup.pressd.repository.CategoryRepository;
import com.codeup.pressd.repository.UserRepository;
import com.codeup.pressd.repository.WorkoutRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
	public String showWorkoutsByCategory(@PathVariable long id, Model viewModel) {

		Category category = categoryDao.getOne(id);
		List<Workout> workouts = workoutDao.getWorkoutsByCategoriesContaining(category);
		viewModel.addAttribute("workouts", workouts);
		return "workouts/categories";
	}

	@GetMapping("/workouts/{id}")
	public String showOneWorkout(@PathVariable long id, Model viewModel) {
		Workout workout = workoutDao.getOne(id);
		viewModel.addAttribute("workout", workout);
		return "workouts/show";
	}

	@GetMapping("/workouts/create")
	public String showCreateWorkout(Model viewModel) {
		viewModel.addAttribute("workout", new Workout());
		return "workouts/create";
	}

	@PostMapping("/workouts/create")
	public String createWorkout(@ModelAttribute Workout workout) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		workout.setUser(user);
		workoutDao.save(workout);
		return "redirect:/workouts";
	}

	@GetMapping("/workouts/{id}/delete")
	public String viewDeleteWorkout(@PathVariable long id, Model viewModel){
		viewModel.addAttribute("workout", workoutDao.getOne(id));
		return "workouts/delete";
	}

	@PostMapping("/workouts/{id}/delete")
	public String deleteWorkout(@PathVariable long id) {
		Workout workout = workoutDao.getOne(id);
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (currentUser.getId() == workout.getUser().getId()) {
			workoutDao.deleteById(id);
		}
		return "redirect:/workouts";
	}

	@GetMapping("/workouts/{id}/update")
	public String showUpdateWorkout (@PathVariable long id, Model viewModel) {
		Workout workout = workoutDao.getOne(id);
		viewModel.addAttribute("workout", workout);
		return "/workouts/update";
	}

	@PostMapping("/workouts/update")
	public String updateWorkout(@ModelAttribute Workout workout) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (currentUser.getId() == workout.getUser().getId()) {
			workoutDao.save(workout);
		}
		return "redirect:/workouts";
	}

}
