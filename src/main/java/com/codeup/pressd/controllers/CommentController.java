package com.codeup.pressd.controllers;

import com.codeup.pressd.models.*;
import com.codeup.pressd.repository.*;
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
}
