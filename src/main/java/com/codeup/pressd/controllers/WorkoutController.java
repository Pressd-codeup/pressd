package com.codeup.pressd.controllers;


import com.codeup.pressd.models.*;
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
    private final ImageRepository imageDao;

    WorkoutController(WorkoutRepository workoutDao, UserRepository userDao, CategoryRepository categoryDao, UserWorkoutRatingRepository userWorkoutRatingDao, RatingRepository ratingDao, ImageRepository imageDao) {
        this.workoutDao = workoutDao;
        this.userDao = userDao;
        this.categoryDao = categoryDao;
        this.userWorkoutRatingDao = userWorkoutRatingDao;
        this.ratingDao = ratingDao;
        this.imageDao = imageDao;
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
        } else {
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
        return "redirect:/workouts/" + id;
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
        return "redirect:/workouts/" + id;
    }

    @GetMapping("/workouts/create")
    public String showCreateWorkout(Model viewModel) {
        viewModel.addAttribute("workout", new Workout());
        return "workouts/create";
    }

    @PostMapping("/workouts/create")
    public String createWorkout(@ModelAttribute Workout workout) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Image image = imageDao.getOne(1L);
        workout.setUser(user);
        workout.setComments(new ArrayList<>());
        workout.setDatePosted(LocalDateTime.now());
        workout.setImage(image);
        workoutDao.save(workout);
        return "redirect:/workouts";
    }

    @GetMapping("/workouts/{id}/delete")
    public String viewDeleteWorkout(@PathVariable long id, Model viewModel) {
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
    public String viewEditWorkout(Model vModel, @PathVariable long id){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getOne(currentUser.getId());
        if (workoutDao.getOne(id).getUser() != user) return "redirect:/workouts";
        Workout workout = workoutDao.getOne(id);
        Image currentImage = workout.getImage();
        vModel.addAttribute("currentImage", currentImage);
        User defaultUser = userDao.getOne(1L);
        List<Image> userImages = imageDao.findImagesByUser(user);
        List<Image> defaultImages = imageDao.findImagesByUser(defaultUser);
        userImages.addAll(defaultImages);
        userImages.remove(currentImage);
        vModel.addAttribute("userImages", userImages);
        vModel.addAttribute("workout", workout);
        return "workouts/update";
    }

    @PostMapping("/workouts/{id}/update")

    public String editWorkout(@ModelAttribute Workout workout, @PathVariable long id, @RequestParam("title") String title, @RequestParam("body") String body, @RequestParam("imageId") long imageId) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getOne(currentUser.getId());
        Workout dbWorkout = workoutDao.getOne(id);
        Image newImage = imageDao.getOne(imageId);
        dbWorkout.setImage(newImage);
        dbWorkout.setTitle(title);
        dbWorkout.setBody(body);
        //user validation is no longer necessary here because it's handled in GetMapping
        workoutDao.save(dbWorkout);

        return "redirect:/workouts";
    }


}
