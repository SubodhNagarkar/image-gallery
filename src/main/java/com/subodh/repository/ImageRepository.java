package com.subodh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subodh.model.ImageModel;
import com.subodh.model.UserModel;

public interface ImageRepository extends JpaRepository<ImageModel, Integer>{
	List<ImageModel> findByUserModel(UserModel userModel);
}
