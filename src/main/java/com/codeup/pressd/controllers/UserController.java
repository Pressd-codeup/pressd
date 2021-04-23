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
    private PasswordEncoder passwordEncoder;
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

    @GetMapping("/users/{id}/editProfile")
    public String profileEditor(@PathVariable long id, Model viewModel) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getOne(currentUser.getId());
        if (!userDao.getOne(id).getUsername().equals(user.getUsername())) return"redirect:/home";


        Image currentImage = (Image) user.getImages();

        User defaultUser = userDao.getOne(1L);


        List<Image> userImages = imageDao.findImagesByUser(user);
        List<Image> defaultImages = imageDao.findImagesByUser(defaultUser);
        userImages.addAll(defaultImages);
        userImages.remove(currentImage);
        viewModel.addAttribute("userImages", userImages);
        viewModel.addAttribute("user", user);
        return "users/editProfile";

    }

    @PostMapping("/users/{id}/editProfile")
    public String saveEditProfile(@ModelAttribute User user, @PathVariable long id, @RequestParam("username") String username, @RequestParam("about") String about, @RequestParam("imageId") long imageId, @RequestParam("email") String email) {


        userDao.save(user);

        return "users/show";
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