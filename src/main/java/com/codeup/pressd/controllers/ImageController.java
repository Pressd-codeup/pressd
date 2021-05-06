package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Image;
import com.codeup.pressd.models.Post;
import com.codeup.pressd.models.User;
import com.codeup.pressd.models.Workout;
import com.codeup.pressd.repository.ImageRepository;
import com.codeup.pressd.repository.PostRepository;
import com.codeup.pressd.repository.UserRepository;
import com.codeup.pressd.repository.WorkoutRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Controller
public class ImageController {

	private final ImageRepository imageDao;
	private final UserRepository userDao;
	private final WorkoutRepository workoutDao;
	private final PostRepository postDao;

	ImageController(ImageRepository imageDao, UserRepository userDao, WorkoutRepository workoutDao, PostRepository postDao) {
		this.imageDao = imageDao;
		this.userDao = userDao;
		this.workoutDao = workoutDao;
		this.postDao = postDao;
	}

	@GetMapping("/users/avatar")
	public String viewAvatarForm(Model viewModel) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userDao.getOne(user.getId());

		//viewModel.addAttribute("user", currentUser);
		long currentImageId = currentUser.getAvatarId();
		Image currentImage = imageDao.getOne(currentImageId);
		viewModel.addAttribute("currentImage", currentImage);

		//viewModel.addAttribute("directory", "avatarImage");

		User defaultUser = userDao.getOne(1L);
		List<Image> userImages = imageDao.findImagesByUser(user);
		List<Image> defaultImages = imageDao.findImagesByUser(defaultUser);
		userImages.addAll(defaultImages);
		userImages.remove(currentImage);
		viewModel.addAttribute("userImages", userImages);

		return "users/avatar";
	}

	@PostMapping("/users/avatar")
	public String changeUserAvatar(@RequestParam("avatarId") long avatarId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long id = user.getId();
		User saveUser = userDao.getOne(id);
		saveUser.setAvatarId(avatarId);
		userDao.save(saveUser);
		return "redirect:/users/" + id;
	}

	/*@GetMapping("/users/workouts")
	public String viewUserWorkouts(Model viewModel) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Workout> workouts = workoutDao.getWorkoutsByUser(user);
		viewModel.addAttribute("workouts", workouts);
		DateTimeFormatter shortF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
		viewModel.addAttribute("shortF", shortF);
		return "users/workouts";
	}

	@GetMapping("/users/posts")
	public String viewUserPosts(Model viewModel) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Post> posts = postDao.getPostsByUser(user);
		viewModel.addAttribute("posts", posts);
		DateTimeFormatter shortF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
		viewModel.addAttribute("shortF", shortF);
		return "users/posts";
	}*/

	@GetMapping("/images/upload")
	public String viewUploadImageForm(@RequestParam("directory") String directory, Model viewModel) {
		System.out.println("UPLOAD FORM GET DIRECTORY: " + directory);
		viewModel.addAttribute("directory", directory);
		return "images/upload";
	}

	//images/upload form should only exist in one place
	//when making a workout/post or changing avatar images, there should be a link on the form (above the image select input?) saying "want to upload an image to use? do it here!" that will link to images/upload
	//(link href="/images/upload/ (workoutImage) (postImage) (avatarImage) (images) etc.)

	/*
	<form id="uploadForm" th:action="@{/images/upload}" th:method="post" style="display: none">
    <input type="hidden" name="url" id="url">
    <input type="submit" value="Upload your image!">
</form>
	 */

	//then, once they've uploaded the image, they will be redirected to where they were before
	//the directory property from the initial link href will be passed to the GetMapping for the upload form, and the upload form action property will be assigned appropriately. Then, the POST of the form will redirect to the proper page via the PostMapping's switch statement.




	@PostMapping("/images/upload")
	public String uploadImage(@RequestParam String url, @RequestParam("directory") String directory) {
	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	Image image = new Image();
		image.setUser(user);
		image.setUrl(url);
		imageDao.save(image);

		String returnVal;

		System.out.println("UPLOAD FORM POST DIRECTORY: " + directory);

		switch(directory) {
			case "createWorkoutImage":
				returnVal = "redirect:/workouts/create";
				break;
			/*case "updateWorkoutImage":
				returnVal = "redirect:/users/workouts";*/ //or whatever the mapping for user workouts page
				//break;
			case "createPostImage":
				returnVal = "redirect:/posts/create";
				break;
			/*case "updatePostImage":
				returnVal = "redirect:/users/posts";*/ //or whatever the mapping for user posts page
				//break;
			case "avatarImage":
				returnVal = "redirect:/users/avatar";
				break;
			case "updateProfileImage":
				returnVal = "redirect:/users/edit";
				break;
			default:
				returnVal = "redirect:/images"; //this is if they access the upload form directly from the Image Center
				break;
		}

		return returnVal;
	}

	@GetMapping("/images/upload/post")
	public String viewUploadImagePostUpdate(@RequestParam("id") long id, Model viewModel) {
		viewModel.addAttribute("id", id);
		return "images/uploadPost";
	}


	@PostMapping("/images/upload/post")
	public String uploadPostUpdateImage(@RequestParam String url, @RequestParam long id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Image image = new Image();
		image.setUser(user);
		image.setUrl(url);
		imageDao.save(image);

		return "redirect:/posts/" + id + "/update";


	}

	@GetMapping("/images/upload/workout")
	public String viewUploadImageWorkoutUpdate(@RequestParam("id") long id, Model viewModel) {
		viewModel.addAttribute("id", id);
		return "images/uploadWorkout";
	}


	@PostMapping("/images/upload/workout")
	public String uploadWorkoutUpdateImage(@RequestParam String url, @RequestParam long id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Image image = new Image();
		image.setUser(user);
		image.setUrl(url);
		imageDao.save(image);

		return "redirect:/workouts/" + id + "/update";


	}
	@GetMapping("/images")
	public String viewAllImages(Model viewModel) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User defaultUser = userDao.getOne(1L);
		List<Image> userImages = imageDao.findImagesByUser(user);
		List<Image> defaultImages = imageDao.findImagesByUser(defaultUser);
		viewModel.addAttribute("userImages", userImages);
		viewModel.addAttribute("defaultImages", defaultImages);
		return "images/index";
	}
}
