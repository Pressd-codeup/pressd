package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.Type;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.PostRepository;
import com.codeup.pressd.repository.UserRepository;
import com.codeup.pressd.repository.TypeRepository;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
public class PostController {

	private final PostRepository postDao;
	private final UserRepository userDao;
	private final TypeRepository typeDao;

	PostController(PostRepository postDao, UserRepository userDao, TypeRepository typeDao){
		this.postDao = postDao;
		this.userDao = userDao;
		this.typeDao = typeDao;
	}

	@GetMapping("/posts")
	public String allPosts(Model viewModel){
		List<Post> posts = postDao.findAll();
		viewModel.addAttribute("posts", posts);
		return "posts/index";
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


	@GetMapping("/posts/create")
	public String showCreatePost(Model viewModel) {
		viewModel.addAttribute("post", new Post());
		return "posts/create";
	}


	@PostMapping("/posts/create")
	public String createPost(@ModelAttribute Post post, @RequestParam(name = "type_id") long type_id){

//		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDao.getOne(2L);
		Type type = typeDao.getOne(type_id);
		post.setUser(user);
		post.setType(type);
		post.setDatePosted(LocalDateTime.now());
		postDao.save(post);
		return "redirect:/posts";
	}

	@GetMapping("/posts/{id}/update")
	public String viewEditForm(Model vModel, @PathVariable long id){
		vModel.addAttribute("post", postDao.getOne(id));
		return "posts/update";
	}

	@PostMapping("/posts/{id}/update")
	public String editPost(@ModelAttribute Post postToUpdate, @PathVariable long id) {

		//WILL NEED AUTHENTICATION OF CURRENTUSER == POSTUSER

		postDao.save(postToUpdate);
		return "redirect:/posts";
	}

	@GetMapping("/posts/{id}/delete")
	public String viewDeletePost(Model vModel, @PathVariable long id){

		vModel.addAttribute("post",postDao.getOne(id));
		return "posts/delete";
	}

	@PostMapping("/posts/{id}/delete")
	public String deletePost(@ModelAttribute Post postToDelete, @PathVariable long id){

		//WILL NEED AUTHENTICATION OF CURRENTUSER == POSTUSER

		postDao.delete(postToDelete);
		return "redirect:/posts";
	}
}