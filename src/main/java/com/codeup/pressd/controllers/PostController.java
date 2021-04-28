package com.codeup.pressd.controllers;

import com.codeup.pressd.models.*;
import com.codeup.pressd.repository.ImageRepository;
import com.codeup.pressd.repository.PostRepository;
import com.codeup.pressd.repository.TypeRepository;
import com.codeup.pressd.repository.UserRepository;
import org.springframework.http.HttpStatus;
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
	private final ImageRepository imageDao;
	private final UserRepository userDao;

	PostController(PostRepository postDao, TypeRepository typeDao, ImageRepository imageDao, UserRepository userDao){
		this.postDao = postDao;
		this.typeDao = typeDao;
		this.imageDao = imageDao;
		this.userDao = userDao;

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
		System.out.println("filter.getType_name() = " + filter.getType_name());
		System.out.println();
		System.out.println("filter.getParams() = " + filter.getParams());
		ArrayList<Post> filteredPosts = new ArrayList<>();
		filteredPosts.add(0,placeHolder);
		for (Post post : posts) {
			if (filter.getParams().contains(String.valueOf(post.getZipcode()))) {
				filteredPosts.add(post);
			}
		}
		if(filteredPosts.size() == 1){
			return "/posts/no_results";
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

		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDao.getOne(currentUser.getId());


		Image currentImage = imageDao.getOne(1L);

		addImages(viewModel, user, currentImage, userDao, imageDao);

		return "posts/create";
	}

	@PostMapping("/posts/create")
	public String createPost(@ModelAttribute Post post, @RequestParam(name = "type_id") long type_id, @RequestParam(name="imageId") long imageId){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Type type = typeDao.getOne(type_id);
		Image image = imageDao.getOne(imageId);
		post.setUser(user);
		post.setType(type);
		post.setImage(image);
		post.setDatePosted(LocalDateTime.now());
		postDao.save(post);
		return "redirect:/posts";
	}

	@GetMapping("/posts/{id}/update")
	public String viewEditForm(Model vModel, @PathVariable long id){
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDao.getOne(currentUser.getId());
		if (postDao.getOne(id).getUser() != user) return "redirect:/posts";

		Post post = postDao.getOne(id);

		addImages(vModel, user, post.getImage(), userDao, imageDao);

		vModel.addAttribute("post", post);

		return "posts/update";
	}


	static void addImages(Model vModel, User user, Image image, UserRepository userDao, ImageRepository imageDao) {
		Image currentImage = image;

		vModel.addAttribute("currentImage", currentImage);

		User defaultUser = userDao.getOne(1L);


		List<Image> userImages = imageDao.findImagesByUser(user);
		List<Image> defaultImages = imageDao.findImagesByUser(defaultUser);
		userImages.addAll(defaultImages);
		userImages.remove(currentImage);
		for (Image i : userImages) {
			System.out.println(i.getId());
		}
		vModel.addAttribute("userImages", userImages);
	}





	@PostMapping("/posts/{id}/update")

	public String editPost(@ModelAttribute Post post, @PathVariable long id, @RequestParam("title") String title, @RequestParam("body") String body, @RequestParam("imageId") long imageId) {


		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDao.getOne(currentUser.getId());

		Post dbPost = postDao.getOne(id);

		Image newImage = imageDao.getOne(imageId);

		dbPost.setImage(newImage);
		dbPost.setTitle(title);
		dbPost.setBody(body);

		//user validation is no longer necessary here because it's handled in GetMapping
		postDao.save(dbPost);

		return "redirect:/posts";
	}


	@PostMapping("/posts/{id}/delete")
	public String deletePost(@PathVariable long id){
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Post postToDelete = postDao.getOne(id);
		if (currentUser.getId() == postToDelete.getUser().getId()) {
			postDao.delete(postToDelete);
		}
		return "redirect:/posts";
	}
}