package com.subodh.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.subodh.model.UserModel;

public interface UserService extends UserDetailsService {

	 UserModel saveUser(UserModel userModel);

	    UserModel getUserById(int userId);

	    void removeSessionMsg();

	    UserModel getUserByEmail(String userEmail);
	    
	    List<UserModel> getAllUsers();
}
