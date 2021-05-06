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
import java.util.Collections;
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
        Collections.reverse(workouts);
        List<Category> allCategories = categoryDao.findAll();
        Category first = allCategories.get(0);
        allCategories.remove(0);
        viewModel.addAttribute("first", first);
        viewModel.addAttribute("allCategories", allCategories);
        viewModel.addAttribute("category", 1);
        viewModel.addAttribute("workouts", workouts);

        return "workouts/index";
    }
    @PostMapping("/workouts")
    public String selectWorkoutsByCategory(@RequestParam String[] categoryNames, Model viewModel) {

        long category = 0;
        List<Workout> allWorkouts = workoutDao.findAll();
        List<Workout> workouts = new ArrayList<>();

        List<Category> categories = new ArrayList<>();
        List<Category> allCategories = categoryDao.findAll();

        for (String catName : categoryNames) {
            for (Category tempCat : allCategories) {
                if (catName.equals(tempCat.getName())) {
                    categories.add(tempCat);
                    break;
                }
            }
        }

        for (Workout tempWork : allWorkouts) {
            for (Category allCatTemp : categories) {
                if (tempWork.getCategories().contains(allCatTemp)) {
                    workouts.add(tempWork);
                    break;
                }
            }
        }


        if (!workouts.isEmpty()) category = 2;
        Category first = allCategories.get(0);
        allCategories.remove(0);
        viewModel.addAttribute("first", first);
        viewModel.addAttribute("allCategories", allCategories);
        viewModel.addAttribute("category", category);
        Collections.reverse(workouts);
        viewModel.addAttribute("workouts", workouts);

        return "workouts/index";
    }

    @GetMapping("/workouts/{id}")
    public String showOneWorkout(@PathVariable long id, Model viewModel) {
        DecimalFormat df = new DecimalFormat("0.00");
        viewModel.addAttribute("df", df);
        Workout workout = workoutDao.getOne(id);
        User user = new User();
        user.setId(999999999);
        boolean isLoggedIn = false;

        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isLoggedIn = true;
            UserWorkoutRating tester = userWorkoutRatingDao.getUserWorkoutRatingByWorkoutAndUser(workout, user);
            long userRating;
            if (tester == null) {
                userRating = 0L;
            } else {
                userRating = userWorkoutRatingDao.getUserWorkoutRatingByWorkoutAndUser(workout, user).getRating().getStars();
                viewModel.addAttribute("uwr", tester);
            }
            viewModel.addAttribute("userRating", userRating);
        } catch (RuntimeException ignored) {
        }
        viewModel.addAttribute("isLoggedIn", isLoggedIn);

        long currentUserId = workout.getUser().getId();

        boolean isUser = (user.getId() == currentUserId);

        viewModel.addAttribute("isUser", isUser);

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
        viewModel.addAttribute("comment", new Comment());
        viewModel.addAttribute("user", user);
        List<Comment> comments = workout.getComments();
        viewModel.addAttribute("comments", comments);
        viewModel.addAttribute("imageDao", imageDao);


        return "workouts/show";
    }

    @PostMapping("/ratings/{id}/create")
    public String createRating(@RequestParam String newRating, @PathVariable long id) {
        long ratingLong = Long.parseLong(newRating);
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
        long ratingLong = Long.parseLong(newRating);
        uwr.setRating(ratingDao.getOne(ratingLong));
        userWorkoutRatingDao.save(uwr);
        return "redirect:/workouts/" + id;
    }

    @GetMapping("/workouts/create")
    public String showCreateWorkout(Model viewModel) {
        viewModel.addAttribute("workout", new Workout());
        List<Category> allCategories = categoryDao.findAll();
        Category first = allCategories.get(0);
        allCategories.remove(0);
        viewModel.addAttribute("first", first);
        viewModel.addAttribute("allCategories", allCategories);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getOne(currentUser.getId());
        Image currentImage = imageDao.getOne(1L);
        PostController.addImages(viewModel, user, currentImage, userDao, imageDao);
        return "workouts/create";
    }

    @PostMapping("/workouts/create")
    public String createWorkout(@ModelAttribute Workout workout, @RequestParam(name="imageId") long imageId, @RequestParam String[] categoryNames) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Image image = imageDao.getOne(imageId);

        List<Category> allCategories = categoryDao.findAll();

        List<Category> categories = new ArrayList<>();

        for (String catName : categoryNames) {
            for (Category tempCat : allCategories) {
                if (catName.equals(tempCat.getName())) {
                    categories.add(tempCat);
                    break;
                }
            }
        }


        workout.setCategories(categories);
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
        List<Category> allCategories = categoryDao.findAll();
        Category first = allCategories.get(0);
        allCategories.remove(0);
        vModel.addAttribute("first", first);
        vModel.addAttribute("allCategories", allCategories);
        Workout workout = workoutDao.getOne(id);
		PostController.addImages(vModel, user, workout.getImage(), userDao, imageDao);
		vModel.addAttribute("workout", workout);
        return "workouts/update";
    }

    @PostMapping("/workouts/{id}/update")
    public String editWorkout(@ModelAttribute Workout workout, @PathVariable long id, @RequestParam("title") String title, @RequestParam("body") String body, @RequestParam("imageId") long imageId, @RequestParam(required=false) String[] categoryNames) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getOne(currentUser.getId());
        Workout dbWorkout = workoutDao.getOne(id);
        Image newImage = imageDao.getOne(imageId);
        dbWorkout.setImage(newImage);
        if (title.length() != 0) dbWorkout.setTitle(title);
        if (body.length() != 0) dbWorkout.setBody(body);

        if (categoryNames != null) {
            List<Category> allCategories = categoryDao.findAll();

            List<Category> categories = new ArrayList<>();

            for (String catName : categoryNames) {
                for (Category tempCat : allCategories) {
                    if (catName.equals(tempCat.getName())) {
                        categories.add(tempCat);
                        break;
                    }
                }
            }


            workout.setCategories(categories);
        }

        workoutDao.save(dbWorkout);

        return "redirect:/workouts/" + id;
    }


}
