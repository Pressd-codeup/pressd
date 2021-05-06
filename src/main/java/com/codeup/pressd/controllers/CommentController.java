package com.codeup.pressd.controllers;


import com.codeup.pressd.models.*;
import com.codeup.pressd.repository.*;
import org.dom4j.rule.Mode;
import org.hibernate.Hibernate;
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
    private final ImageRepository imageDao;

    CommentController(CommentRepository commentDao, UserRepository userDao, WorkoutRepository workoutDao, ImageRepository imageDao) {
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.workoutDao = workoutDao;
        this.imageDao = imageDao;
    }

    @GetMapping("/workouts/{id}/comments")
    public String allComments(@PathVariable long id, Model viewModel) {
        Workout workout = workoutDao.getOne(id);

        List<Comment> comments = workout.getComments();
        viewModel.addAttribute("workout", workout);
        viewModel.addAttribute("comments", comments);
        viewModel.addAttribute("imageDao", imageDao);
        return "comments/index";
    }


    @GetMapping("/workouts/{id}/comments/create")
    public String showCreateComment(@PathVariable long id, Model viewModel) {
        Workout workout = workoutDao.getOne(id);
        viewModel.addAttribute("comment", new Comment());
        return "comments/create";
    }

    @PostMapping("/workouts/{id}/comments/create")
    public String createComment(@PathVariable long id, @ModelAttribute Comment comment, @RequestParam(name = "body") String body) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User dbUser = userDao.getOne(user.getId());
        Workout workout = workoutDao.getOne(id);
        comment.setUser(dbUser);
        comment.setWorkout(workout);
        comment.setDatePosted(LocalDateTime.now());
        List<Comment> allComments = commentDao.findAll();
        long newId = 0;
        for (Comment comment1 : allComments) {
            if (comment1.getId() > newId) newId = comment1.getId();
        }
        ++newId;
        comment.setId(newId);
        commentDao.save(comment);
        return "redirect:/workouts/" + id;
    }

    @GetMapping("/comments/{id}/update")
    public String editCommentForm(@PathVariable long id, Model viewModel) {
        Comment commentFromDb = commentDao.getOne(id);

        viewModel.addAttribute("oldComment", commentFromDb);
        return "comments/update";
    }

    @PostMapping("/comments/{id}/update/{commentId}")
    public String editComment(@PathVariable long id, @PathVariable long commentId, @RequestParam("body") String body) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());

        Comment commentToUpdate = commentDao.getOne(commentId);

        if(body.length() != 0) {
            commentToUpdate.setBody(body);
            commentDao.save(commentToUpdate);
        }

        return "redirect:/workouts/" + id;
    }


    @PostMapping("/comments/{id}/delete/{workoutId}")
    public String deleteComment(@PathVariable long id, @PathVariable long workoutId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentDao.getOne(id);
        if (currentUser.getId() == comment.getUser().getId()) {
            commentDao.deleteById(id);
        }
        return "redirect:/workouts/" + workoutId;

    }

}
