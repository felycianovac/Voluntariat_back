package com.example.demo.User;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserDTO mapToUserDTO(Users user) {
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
//                .organizationId(user.getorganizationId()) //TODO: match organizationId with Organization entity
                .regionId(user.getRegionId())
                .isFirstLogin(user.isFirstLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }


}
