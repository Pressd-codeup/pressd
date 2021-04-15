package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.User;
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

    @GetMapping("/partners")
    public String seeBuddyPosts(Model viewModel){

        List<Post> posts = postDao.getPostsByTypeName("partners");
        viewModel.addAttribute("posts", posts);
        return "posts/index";
    }

    @GetMapping("/coaches")
    public String seeCoachPosts(Model viewModel) {
        List<Post> posts = postDao.getPostsByTypeName("coaches");
        viewModel.addAttribute("posts", posts);
        return "posts/index";
    }

    @GetMapping("/clients")
    public String seeClientPosts(Model viewModel) {
        List<Post> posts = postDao.getPostsByTypeName("clients");
        viewModel.addAttribute("posts", posts);
        return "posts/index";
    }



    @GetMapping("/posts/{id}")
    public String showOnePost(@PathVariable long id, Model viewModel){
        Post post = postDao.getOne(id);
        User user = post.getUser();
        viewModel.addAttribute("post", post);
        viewModel.addAttribute("user", user);
        return "posts/show";
    }
}
