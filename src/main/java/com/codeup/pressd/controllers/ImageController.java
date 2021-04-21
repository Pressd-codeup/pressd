package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Image;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.ImageRepository;
import com.codeup.pressd.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ImageController {

	private final ImageRepository imageDao;
	private final UserRepository userDao;

	ImageController(ImageRepository imageDao, UserRepository userDao) {
		this.imageDao = imageDao;
		this.userDao = userDao;
	}

	@GetMapping("/images/upload/{directory}")
	public String viewUploadImageForm(@PathVariable String directory, Model viewModel) {
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




	@PostMapping("/images/upload/{directory}")
	public String uploadImage(@RequestParam String url, @PathVariable String directory) {
	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	Image image = new Image();
		image.setUser(user);
		image.setUrl(url);
		imageDao.save(image);

		String returnVal;

		switch(directory) {
			case "createWorkoutImage":
				returnVal = "redirect:/workouts/create";
				break;
			case "updateWorkoutImage":
				returnVal = "redirect:/user/workouts"; //or whatever the mapping for user workouts page
				break;
			case "createPostImage":
				returnVal = "redirect:/posts/create";
				break;
			case "updatePostImage":
				returnVal = "redirect:/user/posts"; //or whatever the mapping for user posts page
				break;
			case "avatarImage":
				returnVal = "redirect:/user/avatar";
				break;
			default:
				returnVal = "redirect:/images"; //this is if they access the upload form directly from the Image Center
				break;
		}

		return returnVal;
	}





	/*@PostMapping("/images/upload")
	public String uploadImage(@RequestParam String url) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		*//*String url = imageString.substring(0, imageString.indexOf(","));
		String delete = imageString.substring(imageString.indexOf(",") + 1);*//*
		Image image = new Image();
		image.setUser(user);
		image.setUrl(url);
		imageDao.save(image);
		return "redirect:/images";
	}*/

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
