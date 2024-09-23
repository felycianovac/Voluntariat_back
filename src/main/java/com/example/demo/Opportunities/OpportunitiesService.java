package com.example.demo.Opportunities;


import com.example.demo.Category.Categories;
import com.example.demo.Category.CategoriesRepository;
import com.example.demo.OpportunityCategories.OpportunitiesCategoryRepository;
import com.example.demo.OpportunityCategories.OpportunityCategories;
import com.example.demo.OpportunitySkills.OpportunitySkills;
import com.example.demo.OpportunitySkills.OpportunitySkillsRepository;
import com.example.demo.Organization.Organization;
import com.example.demo.Organization.OrganizationRepository;
import com.example.demo.Region.Regions;
import com.example.demo.Region.RegionsRepository;
import com.example.demo.Sessions.Sessions;
import com.example.demo.Sessions.SessionsRepository;
import com.example.demo.Skills.Skills;
import com.example.demo.Skills.SkillsRepository;
import com.example.demo.User.Users;
import com.example.demo.User.UsersRepository;
import com.example.demo.VolunteerSkills.VolunteerSkills;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpportunitiesService {
    @Autowired
    private OpportunitiesRepository opportunitiesRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private RegionsRepository regionsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private SessionsRepository sessionsRepository;
    @Autowired
    private SkillsRepository skillsRepository;
    @Autowired
    private OpportunitiesCategoryRepository opportunitiesCategoryRepository;
    @Autowired
    private OpportunitySkillsRepository opportunitySkillsRepository;





    public OpportunityResponse createOpportunity(OpportunityRequest request, HttpServletRequest httpRequest) {

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        Regions region = regionsRepository.findById(request.getRegion().getId())
                .orElseThrow(() -> new RuntimeException("Region not found"));

        String email = httpRequest.getUserPrincipal().getName();
        Users createdByUser = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Opportunities opportunity = Opportunities.builder()
                .organization(organization)
                .createdByUser(createdByUser)
                .title(request.getTitle())
                .description(request.getDescription())
                .region(region)
                .address(request.getAddress())
                .isHighPriority(request.isHighPriority())
                .createdAt(new Date())
                .build();

        final Opportunities savedOpportunity = opportunitiesRepository.save(opportunity);

        List<Sessions> sessionsList = request.getSessions().stream()
                .map(sessionDTO -> Sessions.builder()
                        .opportunity(savedOpportunity)
                        .sessionDate(sessionDTO.getDate())
                        .sessionStartTime(sessionDTO.getStartTime())
                        .sessionEndTime(sessionDTO.getEndTime())
                        .spotsLeft(sessionDTO.getSpotsLeft())
                        .build())
                .collect(Collectors.toList());

        sessionsRepository.saveAll(sessionsList);

        List<OpportunityCategories> opportunityCategories = request.getCategories().stream()
                .map(categoryId -> {
                    Categories category = categoriesRepository.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found"));
                    return OpportunityCategories.builder()
                            .opportunity(savedOpportunity)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        opportunitiesCategoryRepository.saveAll(opportunityCategories);

        List<OpportunitySkills> opportunitySkillsList = request.getSkills().stream()
                .map(skillId -> {
                    Skills skill = skillsRepository.findById(skillId)
                            .orElseThrow(() -> new RuntimeException("Skill not found"));
                    return OpportunitySkills.builder()
                            .opportunity(savedOpportunity)
                            .skill(skill)
                            .build();
                })
                .collect(Collectors.toList());

        opportunitySkillsRepository.saveAll(opportunitySkillsList);

        return OpportunityResponse.builder()
                .message("Opportunity created successfully")
                .status("success")
                .build();
    }

    //TODO: modify date format
}
