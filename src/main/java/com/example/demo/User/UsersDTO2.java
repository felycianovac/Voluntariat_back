package com.example.demo.User;

import com.example.demo.Region.RegionsDTO;
import com.example.demo.Skills.SkillsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO2 {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date birthday;
    private List<SkillsDTO> skills;
    private RegionsDTO region;
    private String bio;
    private String profilePicture;

    public static UsersDTO2 fromEntity(Users user) {
        return UsersDTO2.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthday(user.getDateOfBirth() != null ? user.getDateOfBirth() : null)
                .skills(user.getVolunteerSkills().stream()
                        .map(volunteerSkill -> SkillsDTO.fromEntity(volunteerSkill.getSkill()))
                        .collect(Collectors.toList()))
                .region( user.getRegion() != null ? RegionsDTO.fromEntity(user.getRegion()) : null)
                .bio(user.getBio())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}
