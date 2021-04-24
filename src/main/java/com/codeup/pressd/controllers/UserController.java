package com.codeup.pressd.controllers;

import com.codeup.pressd.SecurityConfiguration;
import com.codeup.pressd.models.Image;
import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.User;
import com.codeup.pressd.models.Workout;
import com.codeup.pressd.repository.ImageRepository;
import com.codeup.pressd.repository.PostRepository;
import com.codeup.pressd.repository.UserRepository;
import com.codeup.pressd.repository.WorkoutRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private UserRepository userDao;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageDao;


    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, ImageRepository imageDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.imageDao = imageDao;

    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        User user = new User();
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user, Model model) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setAbout("Tell people about you!");
        user.setMessagesFrom(new ArrayList<>());
        user.setMessagesTo(new ArrayList<>());
        user.setComments(new ArrayList<>());
        user.setPosts(new ArrayList<>());
        user.setDateJoined(LocalDateTime.now());
        user.setAvatarId(1L);
        userDao.save(user);
        return "redirect:login";
    }

    @GetMapping("/users/edit")
    public String profileEditor(Model viewModel) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long currentImageId = currentUser.getAvatarId();
        Image currentImage = imageDao.getOne(currentImageId);
        User defaultUser = userDao.getOne(1L);
        List<Image> userImages = imageDao.findImagesByUser(currentUser);
        List<Image> defaultImages = imageDao.findImagesByUser(defaultUser);
        userImages.addAll(defaultImages);
        userImages.remove(currentImage);
        viewModel.addAttribute("currentImage", currentImage);
        viewModel.addAttribute("user", currentUser);

        return "users/editProfile";

    }

    @PostMapping("/users/edit")
    public String saveEditProfile(@ModelAttribute User user) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        user.setAbout("Tell people about you!");
        user.setPosts(new ArrayList<>());
        user.setDateJoined(LocalDateTime.now());
        user.setAvatarId(1L);
        userDao.save(currentUser);
        return "redirect:/users/show";
    }

    @GetMapping("/users/{id}")
    public String showProfile(@PathVariable long id, Model viewModel) {
        User user = userDao.getOne(id);
        long avatarId = user.getAvatarId();
        Image avatar = imageDao.getOne(avatarId);

        viewModel.addAttribute("user", user);
        viewModel.addAttribute("avatar", avatar);
        return "users/show";
    }


}