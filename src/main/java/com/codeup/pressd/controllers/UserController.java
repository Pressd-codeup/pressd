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
    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageDao;
    private final PostRepository postDao;


    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, ImageRepository imageDao, PostRepository postDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.imageDao = imageDao;
        this.postDao = postDao;
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
        User user = userDao.getOne(currentUser.getId());
        long userId = currentUser.getId();
        long currentUserAvatarId = user.getAvatarId();
        Image currentImage = imageDao.getOne(currentUserAvatarId);
        User defaultUser = userDao.getOne(1L);
        List<Image> userImages = imageDao.findImagesByUser(user);
        List<Image> defaultImages = imageDao.findImagesByUser(defaultUser);
        userImages.addAll(defaultImages);
        userImages.remove(currentImage);

        viewModel.addAttribute("userId", userId);
        viewModel.addAttribute("currentImage", currentImage);
        viewModel.addAttribute("userImages", userImages);
        viewModel.addAttribute("user", user);
        return "users/editProfile";

    }

    @PostMapping("/users/edit")
    public String saveEditProfile(@RequestParam(name = "userId") long userId, @RequestParam(name = "about") String about, @RequestParam(name = "password") String password, @RequestParam(name = "username") String username, @RequestParam(name = "email") String email, @RequestParam(name = "avatarId") long avatarId) {
        User currentUser = userDao.getOne(userId);
        String hash = passwordEncoder.encode(password);
        currentUser.setPassword(hash);
        currentUser.setUsername(username);
        currentUser.setAbout(about);
        currentUser.setEmail(email);
        LocalDateTime dateJoined = currentUser.getDateJoined();
        currentUser.setAvatarId(avatarId);
        currentUser.setDateJoined(dateJoined);
        userDao.save(currentUser);

        return "redirect:/users/" + userId;
    }

    @GetMapping("/users/{id}")
    public String showProfile(@PathVariable long id, Model viewModel) {
        User user = userDao.getOne(id);
        long avatarId = user.getAvatarId();
        Image avatar = imageDao.getOne(avatarId);
        List post = postDao.getPostsByUser(user);

        viewModel.addAttribute("post", post);
        viewModel.addAttribute("user", user);
        viewModel.addAttribute("avatar", avatar);
        return "users/show";
    }


}