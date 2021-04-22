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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


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
		viewModel.addAttribute("post", new Post());
		return "posts/filter";
	}

	@PostMapping("/posts/filter")
	public String showFilteredPosts(@ModelAttribute Filter filter, Model viewModel) {


		List<Post> posts = postDao.getPostsByTypeName(filter.getType_name());
		List<Post> filteredPosts = new List<Post>() {
			@Override
			public int size() {
				return 0;
			}

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public boolean contains(Object o) {
				return false;
			}

			@Override
			public Iterator<Post> iterator() {
				return null;
			}

			@Override
			public Object[] toArray() {
				return new Object[0];
			}

			@Override
			public <T> T[] toArray(T[] a) {
				return null;
			}

			@Override
			public boolean add(Post post) {
				return false;
			}

			@Override
			public boolean remove(Object o) {
				return false;
			}

			@Override
			public boolean containsAll(Collection<?> c) {
				return false;
			}

			@Override
			public boolean addAll(Collection<? extends Post> c) {
				return false;
			}

			@Override
			public boolean addAll(int index, Collection<? extends Post> c) {
				return false;
			}

			@Override
			public boolean removeAll(Collection<?> c) {
				return false;
			}

			@Override
			public boolean retainAll(Collection<?> c) {
				return false;
			}

			@Override
			public void clear() {

			}

			@Override
			public Post get(int index) {
				return null;
			}

			@Override
			public Post set(int index, Post element) {
				return null;
			}

			@Override
			public void add(int index, Post element) {

			}

			@Override
			public Post remove(int index) {
				return null;
			}

			@Override
			public int indexOf(Object o) {
				return 0;
			}

			@Override
			public int lastIndexOf(Object o) {
				return 0;
			}

			@Override
			public ListIterator<Post> listIterator() {
				return null;
			}

			@Override
			public ListIterator<Post> listIterator(int index) {
				return null;
			}

			@Override
			public List<Post> subList(int fromIndex, int toIndex) {
				return null;
			}
		};
		for (Post post : posts) {
			if (filter.getParams().contains(String.valueOf(post.getZipcode()))) {
				filteredPosts.add(post);
			}
		}
		viewModel.addAttribute("posts", filteredPosts);
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