package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.Type;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.PostRepository;
import com.codeup.pressd.repository.UserRepository;
import com.codeup.pressd.repository.TypeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
	public String viewPostForm(Model model){
		model.addAttribute("post", new Post());
		return "posts/create";
	}
	@PostMapping("/posts/create")
	public String createPost(@RequestParam(name = "body") String body,
							 @RequestParam(name = "title") String title,
							 @RequestParam(name = "zipcode") int zipcode,
							 @RequestParam(name = "type_id") int type_id){

//		User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userToAdd = userDao.getOne(1L);
		Post newPost = new Post();
		Type type = new Type(type_id, typeDao.getOne((long)type_id).getName());


		newPost.setUser(userToAdd);
		newPost.setBody(body);
		newPost.setTitle(title);
		newPost.setZipcode(zipcode);
		newPost.setType(type);
		postDao.save(newPost);
		return "redirect:/posts";
	}

	@GetMapping("/posts/{id}/update")
	public String viewEditForm(Model vModel, @PathVariable long id){
		vModel.addAttribute("post",postDao.getOne(id));
		return "posts/update";
	}

	@PostMapping("/posts/{id}/update")
	public String editPost(@ModelAttribute Post postToUpdate,
						   @PathVariable long id,
						   @RequestParam(name = "body") String body,
						   @RequestParam(name = "title") String title,
						   @RequestParam(name = "zipcode") int zipcode,
						   @RequestParam(name = "type_id") int type_id){

//		User userToAdd = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userToAdd = userDao.getOne(1L);
		Type type = new Type(type_id, typeDao.getOne((long)type_id).getName());

		postToUpdate.setId(id);
		postToUpdate.setUser(userToAdd);
		postToUpdate.setBody(body);
		postToUpdate.setTitle(title);
		postToUpdate.setZipcode(zipcode);
		postToUpdate.setType(type);
		postDao.save(postToUpdate);
		return "redirect:/posts";
	}

	@GetMapping("/posts/{id}/delete")
	public String viewDeleteForm(Model vModel, @PathVariable Long id){
		vModel.addAttribute("post",postDao.getOne(id));
		return "posts/delete";
	}

	@PostMapping("/posts/{id}/delete")
	public String deletePost(@ModelAttribute Post postToDelete, @PathVariable Long id){
		postToDelete.setId(id);
		postDao.delete(postToDelete);
		return "redirect:/posts";
	}
}