package com.subodh.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.subodh.model.UserModel;
import com.subodh.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserModel saveUser(UserModel userModel) {

        String encodedPassword = bCryptPasswordEncoder.encode(userModel.getPassword());
        System.out.println(encodedPassword);
        userModel.setPassword(encodedPassword);
        UserModel savedUser = userRepository.save(userModel);
        return savedUser;
    }

    @Override
    public UserModel getUserById(int userId) {
        Optional<UserModel> dbModel = userRepository.findById(userId);
        if (dbModel.isPresent()) {
            return dbModel.get();
        } else {
            return null;
        }
    }

    @Override
    public UserModel getUserByEmail(String userEmail) {

        return userRepository.findByEmail(userEmail);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserModel dbUserModel = userRepository.findByEmail(username);
        if (dbUserModel != null) {
            SimpleGrantedAuthority role = new SimpleGrantedAuthority("USER");
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(role);
            return new User(dbUserModel.getEmail(), dbUserModel.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

    @Override
    public void removeSessionMsg() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpServletRequest request = attr.getRequest();
        HttpSession session = request.getSession();

        session.removeAttribute("msg");

    }

}
