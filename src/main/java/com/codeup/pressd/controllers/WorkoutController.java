package com.codeup.pressd.controllers;


import com.codeup.pressd.models.Category;
import com.codeup.pressd.models.User;
import com.codeup.pressd.models.UserWorkoutRating;
import com.codeup.pressd.models.Workout;
import com.codeup.pressd.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkoutController {

	private final WorkoutRepository workoutDao;
	private final UserRepository userDao;
	private final CategoryRepository categoryDao;
	private final RatingRepository ratingDao;
	private final UserWorkoutRatingRepository userWorkoutRatingDao;

	WorkoutController(WorkoutRepository workoutDao, UserRepository userDao, CategoryRepository categoryDao, UserWorkoutRatingRepository userWorkoutRatingDao, RatingRepository ratingDao) {
		this.workoutDao = workoutDao;
		this.userDao = userDao;
		this.categoryDao = categoryDao;
		this.userWorkoutRatingDao = userWorkoutRatingDao;
		this.ratingDao = ratingDao;

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
		DecimalFormat df = new DecimalFormat("0.00");
		viewModel.addAttribute("df", df);
		Workout workout = workoutDao.getOne(id);
		boolean isLoggedIn = false;

		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			isLoggedIn = true;
			UserWorkoutRating tester = userWorkoutRatingDao.getUserWorkoutRatingByWorkoutAndUser(workout, user);
			long userRating;
			if (tester == null) {
				userRating = 0L;
			} else {
				userRating = userWorkoutRatingDao.getUserWorkoutRatingByWorkoutAndUser(workout, user).getRating().getStars();
				viewModel.addAttribute("uwr", tester);
				System.out.println("UWR: " + tester);
			}
			viewModel.addAttribute("userRating", userRating);
		} catch (RuntimeException ignored) {
		}
		viewModel.addAttribute("isLoggedIn", isLoggedIn);


		List<UserWorkoutRating> uwr = userWorkoutRatingDao.getUserWorkoutRatingsByWorkout(workout);
		long totalRatings = uwr.size();
		double rating;
		if (uwr.isEmpty()) {
			rating = 0.0;
		}else {
			rating = userWorkoutRatingDao.getAverageRating(uwr);
		}
		viewModel.addAttribute("totalRatings", totalRatings);
		viewModel.addAttribute("rating", rating);
		viewModel.addAttribute("workout", workout);
		return "workouts/show";
	}

	@PostMapping("/ratings/{id}/create")
	public String createRating(@RequestParam String newRating, @PathVariable long id) {
		long ratingLong = Long.parseLong(newRating);
		System.out.println("RATINGLONG: " + ratingLong);
		UserWorkoutRating uwr = new UserWorkoutRating();
		Workout workout = workoutDao.getOne(id);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		uwr.setUser(user);
		uwr.setWorkout(workout);
		uwr.setRating(ratingDao.getOne(ratingLong));
		userWorkoutRatingDao.save(uwr);
		return "redirect:/workouts";
	}

	@PostMapping("/ratings/{id}/update")
	public String updateRating(@RequestParam String newRating, @PathVariable long id) {

		Workout workout = workoutDao.getOne(id);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		UserWorkoutRating uwr = userWorkoutRatingDao.getUserWorkoutRatingByWorkoutAndUser(workout, user);
		System.out.println("ID: " + uwr.getId());
		//userWorkoutRatingDao.deleteById(uwr.getId());
		long ratingLong = Long.parseLong(newRating);
		//UserWorkoutRating newUwr = new UserWorkoutRating();

		//newUwr.setUser(user);
		//newUwr.setWorkout(workout);
		//newUwr.setRating(ratingDao.getOne(ratingLong));
		uwr.setRating(ratingDao.getOne(ratingLong));
		userWorkoutRatingDao.save(uwr);
		return "redirect:/workouts";
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
		workout.setComments(new ArrayList<>());
		workout.setDatePosted(LocalDateTime.now());
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
