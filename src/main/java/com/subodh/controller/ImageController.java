package com.subodh.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.subodh.model.ImageModel;
import com.subodh.model.UserModel;
import com.subodh.service.ImageService;
import com.subodh.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
@Controller

public class ImageController {

	 @Autowired
	    private ImageService imageService;

	    @Autowired
	    private UserService userService;

	    private static String uploadDir = "src/main/resources/static/images";

	    @GetMapping("/user/image")
	    public String showImageForm(Model model, HttpSession session) {
	        model.addAttribute("imageObj", new ImageModel());
	        session.setAttribute("imageId", 0);
	        session.setAttribute("imageTitle", "");
	        return "image-form";
	    }

	    @PostMapping("/user/image")
	    public String processImageForm(@Valid @ModelAttribute("imageObj") ImageModel imageModel,
	            BindingResult bindingResult, @RequestParam("imageData") MultipartFile file, HttpSession session)
	            throws Exception {

	        if (bindingResult.hasErrors()) {
	            
	            return "image-form";
	        } else {
	            UserModel sessionUser = (UserModel) session.getAttribute("user");
	            String imageTitle = "";
	            

	            int sessionImageId = (int) session.getAttribute("imageId");
	            String sessionImageTitle = (String) session.getAttribute("imageTitle");
	            if (!file.isEmpty()) {
	               

	                if (sessionImageId != 0) {

	                    String existingTitle = sessionImageTitle;
	                  
	                    Path fileNameAndPath = Paths.get(uploadDir, existingTitle);
	                    System.out.println(fileNameAndPath);
	                    Files.deleteIfExists(fileNameAndPath);

	                }
	              
	                imageTitle = file.getOriginalFilename();
	                Path fileNameAndPath = Paths.get(uploadDir, imageTitle);
	                Files.write(fileNameAndPath, file.getBytes());
	            } else if (imageModel.getImageId() == 0) {
	                session.setAttribute("msg", "Please Add Image");

	                return "image-form";
	            } else {
	                System.out.println("Inside else of file" + imageModel.getImageTitle());
	                imageTitle = sessionImageTitle;

	            }

	            imageModel.setImageTitle(imageTitle);
	            imageModel.setUserModel(sessionUser);
	            imageModel.setImageId(sessionImageId);

	            ImageModel savedImage = imageService.saveImage(imageModel);
	            if (savedImage != null) {
	                if (sessionImageId != 0) {

	                    session.setAttribute("msg", "Image updated successfully");
	                } else {
	                    session.setAttribute("msg", "Image saved successfully");
	                }
	            } else {
	                session.setAttribute("msg", "Something bad happen on server");

	            }
	            return "redirect:/user/image/images";

	        }
	    }

	    @GetMapping("/user/image/images")
	    public String showDashboard(Model model, HttpSession session) {
	        UserModel sessionUser = (UserModel) session.getAttribute("user");

	        List<ImageModel> allImages = imageService.getAllImages(sessionUser);
	        model.addAttribute("allImages", allImages);

	        return "image-dashboard";

	    }

	    @GetMapping("/user/image/delete/{id}")
	    public String deleteImage(@PathVariable int id, HttpSession session) throws Exception {
	        // System.out.println(id);
	        ImageModel dbImage = imageService.getImageById(id);
	        UserModel sessionUser = (UserModel) session.getAttribute("user");
	        if (dbImage != null) {

	            UserModel userImageModel = dbImage.getUserModel();
	            if (userImageModel.getUserId() == sessionUser.getUserId()) {

	                boolean result = false;
	                result = imageService.deleteImage(dbImage);
	                String existingTitle = dbImage.getImageTitle();
	                if (result) {
	                    Path fileNameAndPath = Paths.get(uploadDir, existingTitle);
	                    System.out.println(fileNameAndPath);
	                    Files.deleteIfExists(fileNameAndPath);
	                    session.setAttribute("msg", "Deleted successfully");
	                }
	            } else {

	                session.setAttribute("msg", "You are not authorized to delete other user records.");
	                return "redirect:/";
	            }

	        } else {
	            session.setAttribute("msg", "Image with id = " + id + " doesnot exists in our records!!");
	        }
	        return "redirect:/user/image/images";
	    }

	    @GetMapping("/user/image/update/{id}")
	    public String updateImage(@PathVariable int id, Model model, HttpSession session) throws Exception {
	        
	        ImageModel dbImage = imageService.getImageById(id);
	        if (dbImage != null) {
	            UserModel userImageModel = dbImage.getUserModel();
	            UserModel sessionUser = (UserModel) session.getAttribute("user");
	            if (userImageModel.getUserId() == sessionUser.getUserId()) {

	                session.setAttribute("imageId", id);
	                session.setAttribute("imageTitle", dbImage.getImageTitle());
	                model.addAttribute("imageObj", dbImage);
	                return "image-form";

	            } else {
	                session.setAttribute("msg", "You are not authorized to update other user records.");
	                return "redirect:/";
	            }
	        } else {

	            session.setAttribute("msg", "Image with id = " + id + " doesnot exists in our records!!");
	            return "redirect:/";
	        }

	    }

	}
