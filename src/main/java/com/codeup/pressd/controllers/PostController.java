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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

	static void addImages(Model vModel, User user, Image image, UserRepository userDao, ImageRepository imageDao) {
		Image currentImage = image;

		vModel.addAttribute("currentImage", currentImage);

		User defaultUser = userDao.getOne(1L);


		List<Image> userImages = imageDao.findImagesByUser(user);
		List<Image> defaultImages = imageDao.findImagesByUser(defaultUser);
		userImages.addAll(defaultImages);
		userImages.remove(currentImage);
		vModel.addAttribute("userImages", userImages);
	}

	@GetMapping("/posts")
	public String allPosts(Model viewModel){
		List<Post> posts = postDao.findAll();
		DateTimeFormatter shortF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
		viewModel.addAttribute("shortF", shortF);
		Collections.reverse(posts);
		viewModel.addAttribute("posts", posts);
		viewModel.addAttribute("type", "index");
		return "posts/index";
	}

	@GetMapping("/posts/{id}")
	public String showOnePost(@PathVariable long id, Model viewModel){
		Post post = postDao.getOne(id);
		User user = post.getUser();
		User currentUser = new User();
		currentUser.setId(999999999);
		try {
			currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (RuntimeException ignored) {
		}
		boolean isUser = currentUser.getId() == user.getId();
		viewModel.addAttribute("isUser", isUser);
		viewModel.addAttribute("post", post);
		viewModel.addAttribute("user", user);

		return "posts/show";
	}

	@GetMapping("/partners")
	public String seeBuddyPosts(Model viewModel){

		List<Post> posts = postDao.getPostsByTypeName("partners");
		DateTimeFormatter shortF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
		viewModel.addAttribute("shortF", shortF);
		Collections.reverse(posts);
		viewModel.addAttribute("posts", posts);
		viewModel.addAttribute("type", "partners");
		return "posts/index";
	}

	@GetMapping("/coaches")
	public String seeCoachPosts(Model viewModel) {
		List<Post> posts = postDao.getPostsByTypeName("coaches");
		DateTimeFormatter shortF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
		viewModel.addAttribute("shortF", shortF);
		Collections.reverse(posts);
		viewModel.addAttribute("posts", posts);
		viewModel.addAttribute("type", "coaches");
		return "posts/index";
	}

	@GetMapping("/clients")
	public String seeClientPosts(Model viewModel) {
		List<Post> posts = postDao.getPostsByTypeName("clients");
		DateTimeFormatter shortF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
		viewModel.addAttribute("shortF", shortF);
		Collections.reverse(posts);
		viewModel.addAttribute("posts", posts);
		viewModel.addAttribute("type", "clients");
		return "posts/index";
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
	public String createPost(@ModelAttribute Post post, @RequestParam(name = "type_id") long type_id, @RequestParam(name="imageId") long imageId, @RequestParam String city){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Type type = typeDao.getOne(type_id);
		Image image = imageDao.getOne(imageId);
		post.setCity(city);
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


	@PostMapping("/posts/{id}/update")
	public String editPost(@ModelAttribute Post post, @PathVariable long id, @RequestParam("title") String title, @RequestParam("body") String body, @RequestParam("imageId") long imageId, @RequestParam String city) {

		Post dbPost = postDao.getOne(id);
		Image newImage = imageDao.getOne(imageId);

		if (title.length() != 0) {
			dbPost.setTitle(title);
		}

		if (body.length() != 0) {
			dbPost.setBody(body);
		}

		if (city.length() != 0) {
			dbPost.setCity(city);
		}

		dbPost.setImage(newImage);



		//user validation is no longer necessary here because it's handled in GetMapping
		postDao.save(dbPost);

		return "redirect:/posts/" + id;
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