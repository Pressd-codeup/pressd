package com.codeup.pressd.controllers;

import com.codeup.pressd.SecurityConfiguration;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class UserController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        User user = new User();
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user, Model model){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setAbout("Tell people about you!");
        user.setDateJoined(LocalDateTime.now());
        userDao.save(user);
        return "redirect:users/login";
    }

    @GetMapping("/users/editProfile")
    public String profileEditor(Model viewModel){
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewModel.addAttribute("user", loggedIn);
        return "users/editProfile";

    }
@PostMapping("/users/edit")
    public String saveEditProfile(@ModelAttribute User user){
        userDao.save(user);

    return "users/show";
    }

}
