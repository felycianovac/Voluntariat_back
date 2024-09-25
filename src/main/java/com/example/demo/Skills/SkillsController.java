package com.example.demo.Skills;

import com.example.demo.Region.RegionsDTO;
import com.example.demo.Region.RegionsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin
@AllArgsConstructor
public class SkillsController {
    private final SkillsService skillsService;
    @GetMapping()
    public List<SkillsDTO> getSkills(){
        return skillsService.getSkills();
    }

}