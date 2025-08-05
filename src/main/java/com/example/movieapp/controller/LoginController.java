package com.example.movieapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html を返す
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}



