package com.subodh.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotBlank(message = "please provide username")
    private String userName;

    @NotBlank(message = "please provide email")
    @Email(message = "incorrect email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "please enter password")
    private String password;

    @OneToMany(mappedBy = "userModel")
    private List<ImageModel> imageModel;

}



