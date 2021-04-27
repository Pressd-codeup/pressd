package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.User;
import com.codeup.pressd.models.Workout;
import com.codeup.pressd.repository.ImageRepository;
import com.codeup.pressd.repository.PostRepository;
import com.codeup.pressd.repository.UserRepository;
import com.codeup.pressd.repository.WorkoutRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SearchController {

	private final PostRepository postDao;
	private final WorkoutRepository workoutDao;
	private final UserRepository userDao;
	private final ImageRepository imageDao;


	SearchController(PostRepository postDao, WorkoutRepository workoutDao, UserRepository userDao, ImageRepository imageDao) {
		this.postDao = postDao;
		this.workoutDao = workoutDao;
		this.userDao = userDao;
		this.imageDao = imageDao;
	}

	@PostMapping("/search")
	public String search(@RequestParam(name = "term") String term, Model viewModel) {
		term = "%" + term + "%";
		List<Workout> workouts = workoutDao.findAllByTitleIsLikeOrBodyIsLike(term, term);
		List<Post> posts = postDao.findAllByTitleIsLikeOrBodyIsLike(term, term);
		List<User> users = userDao.findAllByUsernameIsLike(term);
		boolean workoutsFound = !workouts.isEmpty();
		boolean postsFound = !posts.isEmpty();
		boolean usersFound = !users.isEmpty();

		viewModel.addAttribute("workoutsFound", workoutsFound);
		viewModel.addAttribute("postsFound", postsFound);
		viewModel.addAttribute("usersFound", usersFound);

		viewModel.addAttribute("workouts", workouts);
		viewModel.addAttribute("posts", posts);
		viewModel.addAttribute("users", users);
		viewModel.addAttribute("imageDao", imageDao);
		return "search/results";
	}
}
