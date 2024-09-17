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
    private Integer organizationId;
    private int regionId;
    private boolean isFirstLogin;
    private Date createdAt;
    private Date updatedAt;

    public static UserDTO fromEntity(Users user) {
        return UserDTO.builder()
                .id(user.getUserId())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth() != null ? user.getDateOfBirth() : null)
                .bio(user.getBio())
                .profilePicture(user.getProfilePicture())
                .organizationId(user.getCreatedOrganizations() != null && !user.getCreatedOrganizations().isEmpty()
                        ? user.getCreatedOrganizations().get(0).getOrganizationId()
                        : null)
                .regionId(user.getRegionId())
                .isFirstLogin(user.isFirstLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
