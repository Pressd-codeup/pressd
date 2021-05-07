package com.codeup.pressd.controllers;

import com.codeup.pressd.models.CustomError;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model viewModel) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		CustomError error = new CustomError();


		if (status != null) {

			int statusCode = Integer.parseInt(status.toString());
			error.setErrorCode(Integer.toString(statusCode));


			if(statusCode == HttpStatus.NOT_FOUND.value()) {
				error.setErrorMessage("Unfortunately the page you're looking for either doesn't exist or is currently unavailable.");
			}
			else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				error.setErrorMessage("An internal error has occured. Our developers have been notified and are working on the problem.");
			} else {
				error.setErrorMessage("An error has occured. Our developers have been notified and are working on the problem.");
			}
		} else {
			error.setErrorCode("200");
			error.setErrorMessage("Nothing see here...");
		}
		viewModel.addAttribute("error", error);
		return "/error";
	}

	@Override
	public String getErrorPath() {
		return null;
	}
}