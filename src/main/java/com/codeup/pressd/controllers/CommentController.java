package com.codeup.pressd.controllers;


import com.codeup.pressd.models.*;
import com.codeup.pressd.repository.*;
import org.dom4j.rule.Mode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping("/comments/{id}/update")
    public String editComment(@ModelAttribute Comment commentToUpdate, @PathVariable long id) {
        //WILL NEED AUTHENTICATION OF CURRENTUSER == POSTUSER
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentDao.save(commentToUpdate);
        return "redirect:/posts";
    }

    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentDao.getOne(id);

        if (user.getId() == comment.getUser().getId()) {
            commentDao.deleteById(id);
        }
        return "redirect:/workouts/index";
    }
}
