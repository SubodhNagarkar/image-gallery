package com.subodh.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import com.subodh.model.RolesModel;
import com.subodh.model.UserModel;
import com.subodh.repository.RoleRepository;
import com.subodh.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

	 @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private RoleRepository roleRepository;

	    @Autowired
	    private BCryptPasswordEncoder bCryptPasswordEncoder;

	    @Override
	    public UserModel saveUser(UserModel userModel) {

	        String encodedPassword = bCryptPasswordEncoder.encode(userModel.getPassword());
	        System.out.println(encodedPassword);
	        userModel.setPassword(encodedPassword);
	        RolesModel rolesModel = roleRepository.findByRoleName("ROLE_USER").get(0);

	        userModel.setRoles(Arrays.asList(rolesModel));
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
	            
	            Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(dbUserModel.getRoles());
	            return new User(dbUserModel.getEmail(), dbUserModel.getPassword(), authorities);
	        } else {
	            throw new UsernameNotFoundException("Invalid username or password");
	        }
	    }

	    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<RolesModel> roles) {

	        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

	        for (RolesModel tempRole : roles) {
	          
	            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getRoleName());
	            authorities.add(tempAuthority);

	        }
	        return authorities;

	    }

	    @Override
	    public List<UserModel> getAllUsers() {

	        return userRepository.findAll();
	    }

	    @Override
	    public void removeSessionMsg() {
	        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

	        HttpServletRequest request = attr.getRequest();
	        HttpSession session = request.getSession();

	        session.removeAttribute("msg");

	    }

	}
