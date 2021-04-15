//package com.codeup.pressd.controllers;
//
//import com.codeup.pressd.models.Comment;
//import com.codeup.pressd.models.User;
//import com.codeup.pressd.repository.CommentRepository;
//import com.codeup.pressd.repository.PostRepository;
//import com.codeup.pressd.repository.UserRepository;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class CommentController {
//    private UserRepository userDao;
//    private PasswordEncoder passwordEncoder;
//    private CommentRepository commentDao;
//    private PostRepository postDao;
//
//    public CommentController(UserRepository userDao, PasswordEncoder passwordEncoder, CommentRepository commentDao, PostRepository postDao) {
//        this.userDao = userDao;
//        this.passwordEncoder = passwordEncoder;
//        this.commentDao = commentDao;
//        this.postDao = postDao;
//    }
//
//    @PostMapping("/posts/{id}/comment")
//
//    public String editSaveComment(@RequestParam("comment") String comment, @PathVariable long id, Model viewModel) {
//
//        System.out.println("At least it started the comment process");
//        Comment addComment = new Comment();
//        System.out.println("comment = " + comment);
//        addComment.setBody(comment);
//        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        addComment.setUser(author);
//        System.out.println("author.getUsername() = " + author.getUsername());
//        addComment.setPost(postDao.getOne(id));
//        System.out.println("postDao.getOne(id).getTitle() = " + postDao.getOne(id).getTitle());
//        commentDao.save(addComment);
//
//        return "redirect:/posts/" + id;
//    }
//
//}
