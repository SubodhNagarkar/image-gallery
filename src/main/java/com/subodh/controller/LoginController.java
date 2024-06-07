package com.subodh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/user/showLoginForm")
    public String showLoginForm() {
        return "login";
    }
    @GetMapping("/accessdenied")
    public String showDeniedPage() {
        return "access-denied";
    }

}
