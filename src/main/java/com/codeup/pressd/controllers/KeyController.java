package com.codeup.pressd.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class KeyController {

	@Value("${img_key}")
	private String imgApiKey;

	@Value("${zip_key}")
	private String zipApiKey;

	@RequestMapping(path = "/keys.js", produces = "application/javascript")
	@ResponseBody
	public String apikey(){

		return "const imgKey = `" + imgApiKey + "`;" + "const zipKey = `" + zipApiKey + "`;";
	}
}
