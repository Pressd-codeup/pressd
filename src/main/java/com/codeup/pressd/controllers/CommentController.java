package com.codeup.pressd.controllers;

import com.codeup.pressd.models.*;
import com.codeup.pressd.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class CommentController {

	private final CommentRepository commentDao;
	private final UserRepository userDao;
	private final WorkoutRepository workoutDao;

	CommentController(CommentRepository commentDao, UserRepository userDao, WorkoutRepository workoutDao){
		this.commentDao = commentDao;
		this.userDao = userDao;
		this.workoutDao = workoutDao;
	}

	@PostMapping("/comment/create")
	public String createComment(@RequestParam(name = "body") String body,
								@RequestParam(name = "workout_id") long workout_id){
		User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Comment newComment = new Comment();
		Workout workout = workoutDao.getOne(workout_id);
		newComment.setBody(body);
		newComment.setUser(userToAdd);
		newComment.setWorkout(workout);
		workoutDao.save(workout);

		return "workouts/index";
	}
}
