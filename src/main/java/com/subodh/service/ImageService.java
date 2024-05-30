package com.subodh.service;

import java.util.List;

import com.subodh.model.ImageModel;
import com.subodh.model.UserModel;
import com.subodh.model.UserModel;

public interface ImageService {

	ImageModel saveImage(ImageModel imageModel);

    List<ImageModel> getAllImages(UserModel userModel);

    void removeSessionMsg();

    ImageModel getImageById(int id);

    boolean deleteImage(ImageModel dbImage);
}
