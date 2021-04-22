package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Filter;
import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.Type;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.PostRepository;
import com.codeup.pressd.repository.TypeRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@Controller
public class PostController {

	private final PostRepository postDao;
	private final TypeRepository typeDao;

	PostController(PostRepository postDao, TypeRepository typeDao){
		this.postDao = postDao;
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
	@GetMapping("/posts/filter")
	public String filterPosts(Model viewModel){
		viewModel.addAttribute("filter", new Filter());
		return "posts/filter";
	}

	@PostMapping("/posts/filter")
	public String showFilteredPosts(@ModelAttribute Filter filter, Model viewModel) {
		Post placeHolder = new Post();
		List<Post> posts = postDao.getPostsByTypeName(filter.getType_name());
		ArrayList<Post> filteredPosts = new ArrayList<>();
		filteredPosts.add(0,placeHolder);
		for (Post post : posts) {
			if (filter.getParams().contains(String.valueOf(post.getZipcode()))) {
				filteredPosts.add(post);
			}
		}
		if(filteredPosts.size() == 1){
			return "redirect:/posts";
		} else {
			filteredPosts.remove(0);
			viewModel.addAttribute("posts", filteredPosts);
		}
		return "/posts/index";
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
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

	@PostMapping("/posts/update")
	public String editPost(@ModelAttribute Post postToUpdate) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (currentUser.getId() == postToUpdate.getUser().getId()) {
			postDao.save(postToUpdate);
		}
		return "redirect:/posts";
	}

	@GetMapping("/posts/{id}/delete")
	public String viewDeletePost(Model vModel, @PathVariable long id){
		vModel.addAttribute("post",postDao.getOne(id));
		return "posts/delete";
	}

	@PostMapping("/posts/delete")
	public String deletePost(@ModelAttribute Post postToDelete){
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (currentUser.getId() == postToDelete.getUser().getId()) {
			postDao.delete(postToDelete);
		}
		return "redirect:/posts";
	}
}