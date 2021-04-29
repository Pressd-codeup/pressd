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

    CommentController(CommentRepository commentDao, UserRepository userDao, WorkoutRepository workoutDao) {
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.workoutDao = workoutDao;
    }

    @GetMapping("/workouts/{id}/comments")
    public String allComments(@PathVariable long id, Model viewModel) {
        List<Comment> comments = commentDao.findAll();
        viewModel.addAttribute("comments", comments);
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
        long newId = commentDao.findAll().size() + 1;
        comment.setId(newId);
        commentDao.save(comment);
        return "redirect:/workouts/"+id+"/comments";
    }

    @GetMapping("/comments/{id}/update")
    public String editCommentForm(@PathVariable long id, Model viewModel) {
        Comment commentFromDb = commentDao.getOne(id);

        viewModel.addAttribute("oldComment", commentFromDb);
        return "comments/update";
    }

    @PostMapping("/comments/{id}/update")
    public String editComment(@PathVariable Long id, @ModelAttribute Comment commentToUpdate, @RequestParam("body") String body) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getOne(user.getId());

        commentToUpdate.setDatePosted(LocalDateTime.now());
        Workout workout = workoutDao.getOne(id);
        commentToUpdate.setWorkout(workout);
        commentToUpdate.setId(id);
        commentToUpdate.setUser(currentUser);
        commentToUpdate.setBody(body);
        commentDao.save(commentToUpdate);
        return "redirect:/workouts";
    }


    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentDao.getOne(id);
        if (currentUser.getId() == comment.getUser().getId()) {
            commentDao.deleteById(id);
        }
        return "redirect:/workouts";

    }

//    GetMapping("/{id}/comments")
//        public String viewUserComments(@PathVariable long id){
//
//        return ""
//        }
}
