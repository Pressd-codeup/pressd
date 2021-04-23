package com.codeup.pressd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String welcomeDefault() {return "home";}

    @GetMapping("/home")
    public String welcome(){
        return "home";
    }
}
