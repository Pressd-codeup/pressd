package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Post;
import com.codeup.pressd.repository.PostRepository;
import com.codeup.pressd.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PostController {

    private final PostRepository postDao;
    private final UserRepository userDao;

    PostController(PostRepository postDao, UserRepository userDao){
        this.postDao = postDao;
        this.userDao = userDao;
    }

    @GetMapping("/posts")
    public String seeAllPosts(Model viewModel){
        List<Post> postFromDb = postDao.findAll();
        viewModel.addAttribute("posts", postFromDb);
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String showOnePost(@PathVariable Long id, Model viewModel){
        viewModel.addAttribute("post", postDao.getOne(id));
        return "posts/show";
    }
}
