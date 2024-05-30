package com.subodh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subodh.model.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Integer>{

}
