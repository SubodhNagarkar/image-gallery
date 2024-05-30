package com.subodh.service;

import java.util.List;

import com.subodh.model.ImageModel;

public interface ImageService {

	 ImageModel saveImage(ImageModel imageModel);

	    List<ImageModel> getAllImages();

	    void removeSessionMsg();

	    ImageModel getImageById(int id);

	    boolean deleteImage(ImageModel dbImage);
}
