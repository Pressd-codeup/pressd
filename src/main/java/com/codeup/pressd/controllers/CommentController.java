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

	@PostMapping("/comments/create")
	public String createComment(@ModelAttribute Comment comment){
//		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		comment.setUser(user);

		commentDao.save(comment);
		return "redirect:/workouts/index";
	}

	@PostMapping("/comments/{id}/update")
	public String editComment(@ModelAttribute Comment commentToUpdate, @PathVariable long id) {

		//WILL NEED AUTHENTICATION OF CURRENTUSER == POSTUSER

		commentDao.save(commentToUpdate);
		return "redirect:/posts";
	}

	@PostMapping("/comments/{id}/delete")
	public String deleteComment(@ModelAttribute Comment commentToDelete, @PathVariable long id){

		//WILL NEED AUTHENTICATION OF CURRENTUSER == POSTUSER

		commentDao.delete(commentToDelete);
		return "redirect:/workouts/index";
	}
}
