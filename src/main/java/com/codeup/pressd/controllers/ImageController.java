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

	@GetMapping("/images/upload")
	public String viewUploadImageForm() {
		return "images/upload";
	}

	@PostMapping("/images/upload")
	public String uploadImage(@RequestParam String url) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/*String url = imageString.substring(0, imageString.indexOf(","));
		String delete = imageString.substring(imageString.indexOf(",") + 1);*/
		Image image = new Image();
		image.setUser(user);
		image.setUrl(url);
		imageDao.save(image);
		return "redirect:/images";
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
