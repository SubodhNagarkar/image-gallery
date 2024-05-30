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
    public String showImageForm(Model model) {
        model.addAttribute("imageObj", new ImageModel());
        return "image-form";
    }

    @PostMapping("/user/image/{userId}")
    public String processImageForm(@PathVariable int userId, @Valid @ModelAttribute("imageObj") ImageModel imageModel,
            BindingResult bindingResult, @RequestParam("imageData") MultipartFile file, HttpSession session)
            throws Exception {

        UserModel dbModel; // empty object
        if (bindingResult.hasErrors()) {
            // System.out.println("Inside if of error");
            return "image-form";
        } else {
            dbModel = userService.getUserById(userId);
            String imageTitle = "";
            int imageId = imageModel.getImageId();
            if (dbModel != null) {
                if (!file.isEmpty()) {
                    // type = file.getContentType() jpg, png,

                    if (imageId != 0) {

                        String existingTitle = imageModel.getImageTitle();
                        // This case is for update
                        // if update user pass image then delete old image
                        Path fileNameAndPath = Paths.get(uploadDir, existingTitle);
                        System.out.println(fileNameAndPath);
                        Files.deleteIfExists(fileNameAndPath);

                    }
                    // save image
                    imageTitle = file.getOriginalFilename();
                    Path fileNameAndPath = Paths.get(uploadDir, imageTitle);
                    Files.write(fileNameAndPath, file.getBytes());
                } else if (imageModel.getImageId() == 0) {
                    session.setAttribute("msg", "Please Add Image");

                    return "image-form";
                } else {
                    System.out.println("Inside else of file" + imageModel.getImageTitle());
                    imageTitle = imageModel.getImageTitle();

                }

                imageModel.setImageTitle(imageTitle);
                imageModel.setUserModel(dbModel);

                ImageModel savedImage = imageService.saveImage(imageModel);
                if (savedImage != null) {
                    if (imageId != 0) {

                        session.setAttribute("msg", "Image updated successfully");
                    } else {
                        session.setAttribute("msg", "Image saved successfully");
                    }
                } else {
                    session.setAttribute("msg", "Something bad happen on server");

                }
                return "redirect:/user/image/images/"+ userId;
            } else {
                session.setAttribute("msg", "user not found");
                return "redirect:/";
            }

        }
    }

    @GetMapping("/user/image/images/{userId}")
    public String showDashboard(@PathVariable int userId, Model model, HttpSession session) {
        UserModel userModel = userService.getUserById(userId);

        UserModel sessionUser = (UserModel) session.getAttribute("user");
        if (userModel != null && sessionUser.getUserId() == userModel.getUserId()) {
            List<ImageModel> allImages = imageService.getAllImages(userModel);
            model.addAttribute("allImages", allImages);

            return "image-dashboard";
        } else {

            session.setAttribute("msg", "You are not authorized to delete other user records.");
            return "redirect:/";
        }

    }

    @GetMapping("/user/image/delete/{id}/{userId}")
    // if passed string in path variable program fails
    public String deleteImage(@PathVariable int id, @PathVariable int userId, HttpSession session) throws Exception {
        // System.out.println(id);
        ImageModel dbImage = imageService.getImageById(id);
        UserModel userModel = userService.getUserById(userId);
        if (dbImage != null && userModel != null) {

            UserModel userImageModel = dbImage.getUserModel();
            if (userImageModel.getUserId() == userModel.getUserId()) {

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
        return "redirect:/user/image/images/" + userId;
    }

    @GetMapping("/user/image/update/{id}")
    // if passed string in path variable program fails
    public String updateImage(@PathVariable int id, Model model, HttpSession session) throws Exception {
        // System.out.println(id);
        ImageModel dbImage = imageService.getImageById(id);
        if (dbImage != null) {
            UserModel userImageModel = dbImage.getUserModel();
            UserModel sessionUser = (UserModel) session.getAttribute("user");
            if (userImageModel.getUserId() == sessionUser.getUserId()) {

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
