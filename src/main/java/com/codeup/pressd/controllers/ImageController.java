package com.codeup.pressd.controllers;

import com.codeup.pressd.models.Image;
import com.codeup.pressd.models.User;
import com.codeup.pressd.repository.ImageRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ImageController {

	private final ImageRepository imageDao;

	ImageController(ImageRepository imageDao) {
		this.imageDao = imageDao;
	}

	@GetMapping("/images/upload")
	public String viewUploadImageForm() {
		return "images/upload";
	}

	@PostMapping("/images/upload")
	public String uploadImage(@RequestParam String url, @RequestParam String deleteUrl) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/*String url = imageString.substring(0, imageString.indexOf(","));
		String delete = imageString.substring(imageString.indexOf(",") + 1);*/
		Image image = new Image();
		image.setUser(user);
		image.setUrl(url);
		image.setDeleteUrl(deleteUrl);
		imageDao.save(image);
		return "redirect:/images";
	}

	@GetMapping("/images")
	public String viewAllImages(Model viewModel) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Image> images = imageDao.findImagesByUser(user);
		viewModel.addAttribute("images", images);
		return "images/index";
	}
}
