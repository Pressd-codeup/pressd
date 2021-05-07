package com.codeup.pressd.controllers;

import com.codeup.pressd.SecurityConfiguration;
import com.codeup.pressd.models.*;
import com.codeup.pressd.repository.*;
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
    private final WorkoutRepository workoutDao;
    private final CommentRepository commentDao;
    private final MessageRepository messageDao;


    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, ImageRepository imageDao, PostRepository postDao, WorkoutRepository workoutDao, CommentRepository commentDao, MessageRepository messageDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.imageDao = imageDao;
        this.postDao = postDao;
        this.workoutDao = workoutDao;
        this.commentDao = commentDao;
        this.messageDao = messageDao;
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
        user.setMessagesFrom(new ArrayList<>());
        user.setMessagesTo(new ArrayList<>());
        user.setComments(new ArrayList<>());
        user.setPosts(new ArrayList<>());
        user.setDateJoined(LocalDateTime.now());
        user.setAbout("Pressd member since " + user.getDateJoined().getDayOfMonth() + " " + user.getDateJoined().getMonth() + " " + user.getDateJoined().getYear() + ".");
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

        if (password.length() != 0) {
            String hash = passwordEncoder.encode(password);
            currentUser.setPassword(hash);
        }
        if (username.length() != 0) {
            currentUser.setUsername(username);
        }

        if (about.length() != 0) {
            currentUser.setAbout(about);
        }

        if (email.length() != 0) {
            currentUser.setEmail(email);
        }


        LocalDateTime dateJoined = currentUser.getDateJoined();
        currentUser.setAvatarId(avatarId);
        currentUser.setDateJoined(dateJoined);
        userDao.save(currentUser);

        return "redirect:/users/" + userId;
    }

    @GetMapping("/users/{id}")
    public String showProfile(@PathVariable long id, Model viewModel) {
        boolean isLoggedIn = false;
        User currentUser = new User();
        currentUser.setId(999999999);
        try {
            currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isLoggedIn = true;
        } catch (RuntimeException ignored) {
        }
        viewModel.addAttribute("isLoggedIn", isLoggedIn);
        User user = userDao.getOne(id);
        List<Workout> userWorkouts = workoutDao.getWorkoutsByUser(user);
        List<Post> userPosts = postDao.getPostsByUser(user);
        List<Comment> userComments = commentDao.getCommentsByUser(user);
        boolean noWorkouts = userWorkouts.isEmpty();
        boolean noPosts = userPosts.isEmpty();
        boolean noComments = userComments.isEmpty();
        boolean isUser = (currentUser.getId() == id);
        long avatarId = user.getAvatarId();
        Image avatar = imageDao.getOne(avatarId);

        List<Message> messages = messageDao.findAllBySentTo(user);
        boolean unreadMessages = false;
        long messageCount = 0;
        for (Message message : messages) {
            if (message.isRead() == 0) {
                messageCount++;
            }
        }
        if (messageCount > 0) unreadMessages = true;
        viewModel.addAttribute("messageCount", messageCount);
        viewModel.addAttribute("unreadMessages", unreadMessages);
        viewModel.addAttribute("noWorkouts", noWorkouts);
        viewModel.addAttribute("noPosts", noPosts);
        viewModel.addAttribute("noComments", noComments);

        viewModel.addAttribute("isUser", isUser);

        viewModel.addAttribute("user", user);
        viewModel.addAttribute("avatar", avatar);
        return "users/show";
    }

    @GetMapping("/users/profile")
    public String profileFromNav() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = currentUser.getId();
        return "redirect:/users/" + id;
    }

    @GetMapping("/{id}/posts")
    public String showUsersPosts(@PathVariable long id, Model viewModel) {
        User user = userDao.getOne(id);
        List<Post> post = postDao.getPostsByUser(user);
        viewModel.addAttribute("user", user);
        viewModel.addAttribute("posts", post);
        return "users/posts";
    }


    @GetMapping("/{id}/workouts")
    public String showUsersWorkouts(@PathVariable long id, Model viewModel) {
        User user = userDao.getOne(id);
        List<Workout> workout = workoutDao.getWorkoutsByUser(user);
        viewModel.addAttribute("imageDao", imageDao);
        viewModel.addAttribute("user", user);
        viewModel.addAttribute("workouts", workout);
        return "users/workouts";
    }

    @GetMapping("/{id}/comments")
    public String showUsersComments(@PathVariable long id, Model viewModel) {
        User user = userDao.getOne(id);
        List<Workout> workout = workoutDao.getWorkoutsByUser(user);
        List<Comment> comment = commentDao.getCommentsByUser(user);
        viewModel.addAttribute("imageDao", imageDao);
        viewModel.addAttribute("user", user);
        viewModel.addAttribute("comments", comment);
        viewModel.addAttribute("workouts", workout);
        return "users/comments";
    }
}