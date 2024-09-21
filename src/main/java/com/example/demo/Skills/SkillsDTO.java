package com.example.demo.Skills;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillsDTO {
    private int id;
    private String name;

    public static SkillsDTO fromEntity(Skills skill) {
        return SkillsDTO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .build();
    }
}
