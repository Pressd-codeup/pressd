package com.codeup.pressd.controllers;


import com.codeup.pressd.models.*;
import com.codeup.pressd.repository.*;
import org.dom4j.rule.Mode;
import org.hibernate.jdbc.Work;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Controller
public class CommentController {

    private final CommentRepository commentDao;
    private final UserRepository userDao;
    private final WorkoutRepository workoutDao;

    CommentController(CommentRepository commentDao, UserRepository userDao, WorkoutRepository workoutDao) {
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.workoutDao = workoutDao;
    }

    @GetMapping("/workouts/{id}/comments")
    public String allComments(@PathVariable long id, WorkoutRepository workoutDao, Model viewModel) {
        List<Comment> comments = commentDao.findAll();
        List<Workout> workouts = workoutDao.findAllById(id);
        viewModel.addAttribute("comments", comments);
        return "comments/index";
    }


    @GetMapping("/workouts/{id}/comments/create")
    public String showCreateComment(@PathVariable long id, Model viewModel) {
        viewModel.addAttribute("comment", new Comment());
        return "comments/create";
    }

    @PostMapping("/workouts/{id}/comments/create")
    public String createComment(@PathVariable long id, @ModelAttribute Comment comment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Workout workout = workoutDao.getOne(id);
        comment.setUser(user);
        comment.setDatePosted(LocalDateTime.now());
        comment.setWorkout(workout);
        commentDao.save(comment);
        return "redirect:/workouts";
    }

    @GetMapping("/comments/{id}/update")
    public String editCommentForm(@PathVariable long id, Model viewModel) {
        Comment comment = commentDao.getOne(id);
        viewModel.addAttribute("comment", comment);
        return "comments/update";
    }

    @PostMapping("/comments/{id}/update")
    public String editComment(@ModelAttribute Comment comment, @PathVariable long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() == comment.getUser().getId()) {
            commentDao.save(comment);
        } else if (user.getId() != comment.getUser().getId()) {
            return "redirect:/login";
        }
        return "redirect:/workouts";
    }

    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentDao.getOne(id);

        if (user.getId() == comment.getUser().getId()) {
            commentDao.deleteById(id);
        } else if (user.getId() != comment.getUser().getId()) {
            return "redirect:/login";
        }
        return "redirect:/workouts";
    }
}
