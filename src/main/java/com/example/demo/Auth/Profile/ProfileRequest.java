package com.example.demo.Auth.Profile;


import com.example.demo.Region.RegionsDTO;
import com.example.demo.Skills.SkillsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Date birthday;
    private List<SkillsDTO> skills;
    private RegionsDTO region;
    private String bio;
}
