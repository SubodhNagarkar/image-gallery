package com.subodh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.subodh.model.UserModel;
import com.subodh.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String getAllUsers(Model model) {

        List<UserModel> allUsers = userService.getAllUsers();

        model.addAttribute("allUsers", allUsers);

        return "admin-dashboard";

    }

}

