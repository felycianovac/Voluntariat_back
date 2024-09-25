package com.example.demo.Skills;

import com.example.demo.Region.RegionsDTO;
import com.example.demo.Region.RegionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillsService {
    @Autowired
    private SkillsRepository skillsRepository;

    public List<SkillsDTO> getSkills(){

        return skillsRepository.findAll().stream()
                .map(SkillsDTO::fromEntity)
                .toList();
    }
}

