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
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private String bio;
    private String profilePicture;
    private Integer organizationId;
    private Integer regionId;
    private boolean isFirstLogin;
    private Date createdAt;
    private Date updatedAt;

    public static UserDTO fromEntity(Users user) {
        return UserDTO.builder()
                .id(user.getUserId())
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
                .regionId(user.getRegion() != null ? user.getRegion().getRegionId() : null)
                .isFirstLogin(user.isFirstLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
