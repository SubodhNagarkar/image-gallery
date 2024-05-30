package com.subodh.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.subodh.model.UserModel;

public interface UserService extends UserDetailsService {

    UserModel saveUser(UserModel userModel);

    void removeSessionMsg();

    UserModel getUserByEmail(String userEmail);
}
