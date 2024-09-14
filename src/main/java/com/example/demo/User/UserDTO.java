package com.example.demo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int id;
    private Roles role;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private String bio;
    private String profilePicture;
    private int organizationId;
    private int regionId;
    private boolean isFirstLogin;
    private Date createdAt;
    private Date updatedAt;
}
