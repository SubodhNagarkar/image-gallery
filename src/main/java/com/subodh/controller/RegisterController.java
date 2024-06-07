package com.subodh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.subodh.model.UserModel;
import com.subodh.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class RegisterController {

	 private UserService userService;

	    public RegisterController(UserService userService) {
	        this.userService = userService;
	    }

	    @GetMapping("/user/register")
	    public String showRegistrationForm(Model model) {
	        model.addAttribute("userObj", new UserModel());
	        return "registration-form";
	    }

	    @PostMapping("/user/signup")
	    public String processRegistrationForm(@Valid @ModelAttribute("userObj") UserModel userModel,
	            BindingResult bindingResult, HttpSession session) {

	        if (bindingResult.hasErrors()) {
	            return "registration-form";
	        } else {
	            System.out.println("inside registration method, " + userModel);
	            userService.saveUser(userModel);
	            session.setAttribute("msg", "user saved succesffully");
	        }
	        return "redirect:/user/showLoginForm";
	    }

}
