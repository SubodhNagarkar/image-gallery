package com.subodh.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.subodh.model.UserModel;
import com.subodh.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

    	 String userEmail = authentication.getName();

         UserModel dbUser = userService.getUserByEmail(userEmail);

         HttpSession session = request.getSession();
         session.setAttribute("user", dbUser);

         response.sendRedirect(request.getContextPath() + "/user/image/images");

        

    }

}
